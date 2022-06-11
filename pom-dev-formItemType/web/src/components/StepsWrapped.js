import React, { useState, useRef } from 'react';
import { Steps, Button, message } from 'antd';
import Content from '@/layouts/Content';
import Flex from '@/../zero-antd-dep/layout/Flex';
import { RollbackOutlined, LeftOutlined, RightOutlined, CheckOutlined } from '@ant-design/icons';
import { history, withRouter } from 'umi';
import { useDidMount } from 'zero-element/lib/utils/hooks/lifeCycle';
import { query } from 'zero-element-antd/lib/utils/request';
import { useModel, setPageData, getPageData } from 'zero-element/lib/Model';
import { formatAPI } from 'zero-element/lib/utils/format';

const { Step } = Steps;
const { FlexItem } = Flex;

export default withRouter(function StepsWrapped(props) {
  const {
    namespace, children,
    steps = [], nextBefore,
    extraData,
    location,
    onSubmit,
    API,
  } = props;
  const { id } = location.query;
  const [current, setCurrent] = useState(0);
  const [loading, setLoading] = useState(Boolean(API));
  const formRef = useRef();
  const currentRef = useRef(current);
  const model = useModel(namespace);

  useDidMount(_ => {
    if (API) {
      model.fetchOne({
        API: formatAPI(API, { namespace }),
      })
        .then(data => {
          if (data.data) {
            setPageData(namespace, 'formData', data.data);
          }
        })
        .finally(_ => {
          setLoading(false);
        })
    }
  })

  function goBack() {
    history.goBack();
  }
  function onPerv() {
    setCurrent(current - 1);
    currentRef.current = current - 1;
  }
  function onNext(data) {
    let formData;
    if (data) {
      formData = data;
    } else {
      formData = {
        ...model.formData,
        ...formRef.current.getFieldsValue(),
      };
    }
    model.save('formData', formData);
    setPageData(namespace, 'formData', formData);
    setCurrent(current + 1);
    currentRef.current = current + 1;
  }
  function triggerSubmit() {
    formRef.current.submit();
  }

  function handleSubmit(submitData) {
    const func = nextBefore[currentRef.current];
    const formData = getPageData(namespace).formData;
    const data = {
      ...formData,
      ...submitData,
    };
    if (typeof func === 'function') {
      Promise.resolve()
        .then(_ => {
          const rst = func(data);
          if (rst instanceof Promise) {
            setLoading(true);
          }
          return rst;
        })
        .then(handleNext)
        .catch(_ => void 0)
        .finally(_ => setLoading(false))
    } else {
      handleNext(data);
    }

  }

  function handleNext(data) {
    if (currentRef.current === steps.length - 1) {
      Promise.resolve()
        .then(_ => setLoading(true))
        .then(_ => onSubmit(data))
        .then(_ => {
          message.success('操作成功');
          goBack();
        })
        .finally(_ => setLoading(false))
    } else {
      onNext(data);
    }
    return Promise.resolve();
  }

  const child = React.Children.toArray(children)[current];

  return <Content>
    <Steps current={current}>
      {steps.map(step => <Step key={step.title} title={step.title} description={step.description} />)}
    </Steps>
    <div style={{ minHeight: 570 }}>
      {!loading && React.cloneElement(child, {
        namespace,
        onSubmit: handleSubmit,
        formRef,
        extraData: {
          ...extraData,
        },
      })}
    </div>
    <Flex>
      <FlexItem flex={1}></FlexItem>
      <FlexItem>
        <Button icon={<RollbackOutlined />}
          onClick={goBack}
        >取消</Button>
        {current > 0 ? (
          <Button icon={<LeftOutlined />}
            className="ZEleA-margin-left"
            onClick={onPerv}
          >上一步</Button>
        ) : null}
        {current < (steps.length - 1) ? (
          <Button
            type="primary"
            icon={<RightOutlined />}
            className="ZEleA-margin-left"
            loading={loading}
            onClick={triggerSubmit}
          >下一步</Button>
        ) : null}
        {current === (steps.length - 1) ? (
          <Button
            icon={<CheckOutlined />}
            className="ZEleA-margin-left"
            type="primary"
            onClick={triggerSubmit}
            loading={loading}
          >完成</Button>
        ) : null}
      </FlexItem>
    </Flex>
  </Content>
})