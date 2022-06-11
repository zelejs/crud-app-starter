import React, { useRef, useMemo, useState, useEffect } from 'react';
import { Form } from 'antd';
import { history } from 'umi';
import { formatAPI } from 'zero-element/lib/utils/format';
import useBaseForm from 'zero-element/lib/helper/form/useBaseForm';
import { useDidMount, useWillUnmount, useForceUpdate } from 'zero-element/lib/utils/hooks/lifeCycle';
import { Spin, Button, message } from 'antd';
import { getFormItem } from 'zero-element-antd/lib/utils/readConfig';
import { Render } from 'zero-element/lib/config/layout';
import global from 'zero-element/lib/config/global';
import useFormHandle from 'zero-element-antd/lib/container/Form/utils/useFormHandle';
import extraFieldType from 'zero-element-antd/lib/container/Form/utils/extraFieldType';
import canPortal from 'zero-element-antd/lib/utils/canPortal';
import { setPageData, getPageData, clearPageData, getHooks } from 'zero-element/lib/Model';

import promiseAjax from '@/utils/promiseAjax';
import { get as getEndpoint } from 'zero-element/lib/utils/request/endpoint';

import './index.css';
import useSelectedKeys from '@/framework/PrimaryLayout/utils/useSelectedKeys';

const defaultLabelCol = {
  xs: { span: 8, },
};
const defaultWrapperCol = {
  // xs: { span: 16, },
};
export default function CustomtForm(props) {
  const [form] = Form.useForm();

  const forceUpdate = useForceUpdate();
  const {
    MODAL, namespace, config, extraData = {},
    onClose, onSubmit, extraEl,
    loading: propsLoading,
    forceInitForm,
    footer,
    hooks = {},
    formRef,
    keepData = false, // 卸载 BaseForm 时不销毁 model.formData
  } = props;
  const {
    API = {},
    layout = 'Empty', layoutConfig = {},
    fields: fieldsCfg,
    path,
    goBack: gobackOpt = true,
    footer: footerOpt,
    requestOptions,
    otherProps = {},
  } = config;
  const { layoutType = 'inline' } = layoutConfig; // inline vertical horizontal
  const formProps = useBaseForm({
    namespace,
    modelPath: 'formData',
    extraData,
  }, config);
  const { router, goBack } = global;

  const renderGoBack = extraEl && extraEl.current && goBack;

  const { loading, data, model, handle } = formProps;
  const { onGetData, onFormMap } = getHooks(namespace);
  const pageDataFormData = getPageData(namespace).formData;

  //保存当前表单ID
  const [custActivityId, setCustActivityId] = useState('');
  //保存当前流程ID
  const [custWorkFlowId, setCustWorkFlowId] = useState('');
  //申请状态
  const [applyStatus, setApplyStatus] = useState('START');

  //新增属性
  const { footerButton = true, submitBtnText = '保存', isApplied = false,
    applyFormFileds, applyHistoryFileds, pageType = '' } = otherProps;

  const initData = useRef({
    ...extraData,
    ...pageDataFormData,
    ...data,
  });

  //审核信息JSON配置
  let applyFormFiledsConf = applyFormFileds;
  if (applyFormFiledsConf && Array.isArray(initData.current.nextSteps)) {
    handleApplyFormFormat();
  }

  const {
    onFormatValue,
    handleFormatValue,
    onSaveOtherValue,
    onValuesChange,
    onExpect,
  } = useFormHandle(form, {
    namespace,
    config,
    forceInitForm,
    onGetOne: handleGetData,
  });
  const extraFields = useRef([]);
  const [fields, setFields] = useState(fieldsCfg);
  const { onGetOne, onCreateForm, onUpdateForm, onClearForm } = handle;
  const [canRenderForm, setCanRenderForm] = useState(API.getAPI ? false : true);

  // useMemo(recordDefaultValue, [fields]);
  useDidMount(_ => {
    recordDefaultValue();
    if (API.getAPI) {
      handleGetData();
    }
    if (typeof formRef === 'object') {
      formRef.current = form;
    }
    //显示隐藏UI
    if (pageType == 'CREATOR') {
      applyFormFiledsConf = [];
    }
  });

  useWillUnmount(_ => {
    // if (!keepData) {
    if (!keepData || MODAL) {
      onClearForm();
      clearPageData(namespace, 'formData');
    }
  });

  //处理经办人是否必填
  function handleApplyFormFormat() {
    const currentUserName = (initData.current.nextSteps[0] && initData.current.nextSteps[0].currentUserName) || initData.current.currentUserName;
    applyFormFiledsConf.map((item, index) => {
      if (item.field == 'currentUserId') {
        if (currentUserName) {
          item.rules = [];
          return item;
        } else {
          item.rules = ["required"];
          return item;
        }
      }
    })
  }

  //获取api数据
  function handleGetData() {
    setCanRenderForm(false);
    onGetOne({}).then((response) => {
      const { code, data } = response || {};
      if (code === 200 && data) {
        let formData = data;
        if (typeof onGetData === 'function') {
          formData = onGetData(data);
        }

        initData.current = formData;
        const { extra, images, tags } = formData;
        setPageData(namespace, 'formData', formData);
        form.setFieldsValue({ ...initData.current });

        if (Array.isArray(images) || Array.isArray(tags)) {
          const otherField = [];

          if (Array.isArray(tags)) {
            otherField.push({ lable: '', field: 'tags', type: 'tags' });
          }
          if (Array.isArray(images)) {
            otherField.push({ lable: '', field: 'images', type: 'upload-image' });
          }

          setFields([
            ...fields,
            ...otherField,
          ]);
        }
        if (extra && Array.isArray(extra.items)) {
          setExtraFields(extra.items);
        } else {
          forceUpdate();
        }

        //保存表单ID
        setCustActivityId(data.formType);
        //保存流程ID
        setCustWorkFlowId(data.id);
        //保存申请单状态
        setApplyStatus(data.status);

        //根据表单ID获取 页面渲染json配置信息
        //isApplied 是否查看申请详情, 如 false 则获取流程表单数据

        const applyFormInfo = handleApplyFormInfo(data.formInfo);

        if (!isApplied) {
          handleGetActivitiesLayout(applyFormInfo);
        } else {

          //我的申请，待办事项详情 获取申请信息
          if(API.getApplyInfoAPI){
            handleGetFormInfo(data.id);
          }

          handleGetApplyHistory(data.id);

          if (data.status == 'CLOSE_REJECTED' || data.status == 'CLOSE_APPROVED') {
            setFields([
              ...applyFormInfo,
              { "field": "_group", "type": "group-title", "defaultValue": "审批历史" },
              applyHistoryFileds,
            ])
          } else {
            setFields([
              ...applyFormInfo,
              { "field": "_group", "type": "group-title", "defaultValue": "审批历史" },
              applyHistoryFileds,
              ...applyFormFiledsConf
            ])
          }

        }
      }
    })
      .finally(_ => {
        setCanRenderForm(true);
      })
  }

  function handleApplyFormInfo(formInfo) {
    if (formInfo && JSON.stringify(formInfo) != '{}') {

      const layoutJson = formInfo.layoutJson;

      const fieldList = [];

      layoutJson.map(item => {

        if (typeof item.title == 'string') {
          const titleGroud = { "field": "_group", "type": "group-title", "defaultValue": `${item.title}` }
          fieldList.push(titleGroud);
        }

        if (Array.isArray(item.items) && item.items.length > 0) {
          item.items.map(item => {
            fieldList.push(item);
          })
        }
      })

      return fieldList;

    } else {
      return [];
    }
  }

  //获取表单页面配置数据 -- 旧代码
  function handleGetActivities(activityId) {
    // const getFieldsAPI = API.getFieldsAPI;
    // const formatApi = getFieldsAPI.replace('(id)', activityId);

    // const apiUrl = `${getEndpoint()}${formatApi}`
    // const queryData = {
    // }

    // promiseAjax(apiUrl, queryData)
    //   .then(resp => {

    //     if (resp && resp.code === 200) {
    //       const data = resp.data;
    //       if (Array.isArray(data.layoutJson)) {
    //         setFields([
    //           { "field": "_group", "type": "group-title", "defaultValue": "申请信息" },
    //           ...data.layoutJson,
    //           ...applyFormFiledsConf
    //         ])
    //       }

    //     } else {
    //       console.log('获取页面配置信息失败')
    //     }
    //   })
  }
  
  function handleGetActivitiesLayout(data) {
    if (Array.isArray(data)) {
      setFields([
        ...data,
        ...applyFormFiledsConf
      ])
    }
  }

  //获取审核历史数据
  function handleGetFormInfo(id) {
    let path = API.getApplyInfoAPI;
    if(path && path.indexOf('(id)' != -1)){
      path = path.replace('(id)', id);
    }
    const apiUrl = `${getEndpoint()}${path}`
    const queryData = {
    }
    promiseAjax(apiUrl, queryData)
      .then(resp => {

        if (resp && resp.code === 200) {
          const data = resp.data;
          form.setFieldsValue({ ...data });
        } else {
          console.log('获取申请信息失败')
        }
      })
  }

  //获取审核历史数据
  function handleGetApplyHistory(id) {
    const getApplyHistoryAPI = API.getApplyHistoryAPI;
    const apiUrl = `${getEndpoint()}${getApplyHistoryAPI}`
    const queryData = {
      instanceId: id
    }

    promiseAjax(apiUrl, queryData)
      .then(resp => {

        if (resp && resp.code === 200) {
          const data = resp.data;
          // setApplyHistory(data)
          initData.current.history = data;
          form.setFieldsValue({ ...initData.current });
        } else {
          console.log('获取审核历史信息失败')
        }
      })
  }

  //创建流程
  function handleCreateApply(subData) {
    const createApplyAPI = API.createApplyAPI;
    const formatApi = createApplyAPI.replace('(id)', custActivityId);

    const apiUrl = `${getEndpoint()}${formatApi}`
    const queryData = subData;
    queryData.processId = initData.current.id;

    // delete queryData.steps;
    // delete queryData.nextSteps;
    // console.log('queryData = ', queryData);

    promiseAjax(apiUrl, queryData, { method: 'POST' })
      .then(resp => {
        if (resp && resp.code === 200) {
          const data = resp.data;
          // console.log('提交申请成功 response = ', data)
          //返回上一页
          window.history.back();
        } else {
          console.log('提交申请失败')
        }
      })
  }

  //提交审批
  function handleUpdateApplyAPI(subData) {

    const { approveUrl, rollbackUrl, rejectUrl, publicApplyUrl } = API.updateApplyAPI;

    let formatApi = publicApplyUrl;
    if(publicApplyUrl.indexOf('(id)') != -1){
      formatApi = formatApi.replace('(id)', custWorkFlowId);
    }
    const queryData = subData;
    queryData.processId = initData.current.id;

    // if (subData.auditInfo == 'APPROVE') {
    //   formatApi = approveUrl.replace('(id)', custWorkFlowId);
    // } else if (subData.auditInfo == 'ROLLBACK') {
    //   formatApi = rollbackUrl.replace('(id)', custWorkFlowId);
    // } else {
    //   formatApi = rejectUrl.replace('(id)', custWorkFlowId);
    // }

    const apiUrl = `${getEndpoint()}${formatApi}`;

    promiseAjax(apiUrl, queryData, { method: 'POST' })
      .then(resp => {
        if (resp && resp.code === 200) {
          const data = resp.data;
          console.log('提交成功 response = ', data)
          //返回上一页
          window.history.back();
        } else {
          console.log('提交失败')
        }
      })
  }

  //根据返回的 extra 信息处理页面json
  function setExtraFields(items) {
    setFields([
      ...fields,
      ...items.map((item, i) => {
        extraFields.current.push(item.attr);
        return {
          label: item.fieldName,
          field: ['extra', 'items', String(i), 'value'],
          type: extraFieldType[item.fieldType] || 'input',
          value: ['extra', 'items', String(i), 'value'],
        }
      }),
    ]);
  }

  function recordDefaultValue() {
    fields.forEach(item => {
      const { field, value } = item;
      if (value !== undefined && initData.current[field] === undefined) {
        initData.current[field] = value;
      }
    });
    form.setFieldsValue({ ...initData.current });
    forceUpdate();
  }

  function handleSubmitForm(values) {
    const extraSubmit = {};
    fields.forEach(field => {
      if (field.type === 'hidden') {
        extraSubmit[field.field] = extraData[field.field] || field.value;
      }
    })
    let submitData = {
      ...extraSubmit,
      ...pageDataFormData,
      ...values,
    };

    // console.log('submitData = ', submitData)
    // return;

    handleFormatValue(submitData);

    // 修改并提交 extra 里面的数据
    if (extraFields.current.length) {
      const extraData = submitData.extra.items;
      submitData.extra.items = pageDataFormData.extra.items;

      extraFields.current.forEach(field => {
        const index = submitData.extra.items.findIndex(item => item.attr === field);
        const find = submitData.extra.items[index];

        if (find) {
          find.value = extraData[index].value;
        }
      });
    }

    if (typeof onSubmit === 'function') {
      onSubmit(submitData, handleResponse);
      return false;
    }

    if (typeof onFormMap === 'function') {
      submitData = onFormMap(submitData, pageDataFormData);
    }

    if (API.updateAPI) {
      onUpdateForm({
        fields: submitData,
        options: requestOptions,
      }).then(handleResponse);
    } else if (API.createApplyAPI) {
      handleCreateApply(submitData);
    } else if (API.updateApplyAPI) {
      handleUpdateApplyAPI(submitData);
    } else {
      onCreateForm({
        fields: submitData,
        options: requestOptions,
      }).then(handleResponse);
    }
  }

  function handleResponse(data = {}, opt = {}) {
    const { message: msg = '操作成功' } = opt;
    if (data.code === 200) {
      if (msg) {
        message.success(msg);
      }
      if (onClose) {
        onClose(data);
      }
      if (router) {
        if (path) {
          const fPath = formatAPI(path, {
            namespace,
          });
          router(fPath);
          return false;
        }
      }
      if (!MODAL && gobackOpt && goBack) {
        goBack();
      }
    } else {
      message.error(`操作失败: ${data.message}`);
    }
  }

  function handleGoBack() {
    if (path) {
      const fPath = formatAPI(path, {
        namespace,
      });
      router(fPath);
    } else {
      goBack();
    }
  }

  function handleReset() {
    form.resetFields();
    forceUpdate();
  }

  //保存按钮点击事件
  function renderFooter() {
    function onSubmit() {
      form.submit();
    }

    if (footer !== undefined || footerOpt !== undefined) {
      return footer;
    }

    const classes = MODAL ? 'ant-modal-footer' : 'ZEle-Form-footer';
    return <div className={classes}>
      <Button onClick={handleReset}>重置</Button>
      <Button type="primary" htmlType="submit" onClick={onSubmit}>{submitBtnText}</Button>
    </div>
  }

  return <Spin spinning={propsLoading || loading}>
    {renderGoBack && canPortal(extraEl, <Button onClick={handleGoBack}>返回</Button>)}
    <div className={fields.length ? 'ant-modal-body' : undefined}>
      {canRenderForm ? (
        <Form
          form={form}
          layout={layoutType}
          labelCol={defaultLabelCol}
          wrapperCol={defaultWrapperCol}
          initialValues={initData.current}
          onValuesChange={onValuesChange}
          onFinish={handleSubmitForm}
        >
          <Render n={layout} {...layoutConfig}>
            {fields.map(field => getFormItem(field, model, {
              namespace,
              form,
              handle: {
                onFormatValue,
                onSaveOtherValue,
                onExpect,
              },
              hooks,
              extraData,
            }))}
          </Render>
        </Form>
      ) : <Form form={form} />}
    </div>

    {footerButton && applyStatus != 'CLOSE_REJECTED' && applyStatus != 'CLOSE_APPROVED' ? (renderFooter()) : null}
  </Spin>
}