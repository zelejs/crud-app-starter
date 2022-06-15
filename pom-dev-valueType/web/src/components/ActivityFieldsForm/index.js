import React, { useRef, useMemo, useState } from 'react';
import { Form } from 'antd';
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
import './index.css';

const defaultLabelCol = {
    xs: { span: 8, },
};
const defaultWrapperCol = {
    // xs: { span: 16, },
};
export default function BaseForm(props) {
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

    const initData = useRef({
        ...extraData,
        ...pageDataFormData,
        ...data,
    });

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

    
    //新增属性
     const { fieldForIdKey="entityId" } = otherProps;

     console.log('fieldForIdKey = ', fieldForIdKey)

    const [urlId, setUrlId] = useState('');

    // useMemo(recordDefaultValue, [fields]);
    useDidMount(_ => {
        recordDefaultValue();

        const searchList = location.search.split('=');
        const id = searchList[1];
        setUrlId(id)
        if (API.getAPI) {
            handleGetData();
        }
        if (typeof formRef === 'object') {
            formRef.current = form;
        }
    });

    useWillUnmount(_ => {
        // if (!keepData) {
        if (!keepData || MODAL) {
            onClearForm();
            clearPageData(namespace, 'formData');
        }
    });

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

                    if (Array.isArray(images)) {
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
            }
        })
            .finally(_ => {
                setCanRenderForm(true);
            })
    }
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

        submitData[fieldForIdKey] = urlId;

        if (API.updateAPI) {
            onUpdateForm({
                fields: submitData,
                options: requestOptions,
            }).then(handleResponse);
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
            <Button type="primary" htmlType="submit" onClick={onSubmit}>保存</Button>
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
        {renderFooter()}
    </Spin>
}