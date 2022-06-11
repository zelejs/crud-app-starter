import React from 'react';
import { Tabs } from 'antd';
import { history, withRouter } from 'umi';
import Flex from '@/../zero-antd-dep/layout/Flex';

const { TabPane } = Tabs;
const { FlexItem } = Flex;

function ActionOnTabs(props) {
  const { options = {}, handle = {} } = props;
  const { all, field, tabs = [] } = options;
  const { onGetList } = handle;

  function handleClick(key) {

    if (typeof onGetList === 'function') {
      const queryData = {
        [field]: key,
      };
      if (key === 'all') {
        queryData[field] = undefined;
      }
      onGetList({
        queryData,
      })
        .then(data => {
          console.log('get list response', data);

        })
    }
  }

  return <FlexItem flex={1}>
    <Tabs defaultActiveKey={all ? 'all' : tabs[0] && tabs[0].value} onChange={handleClick}>
      {all ? <TabPane tab="全部" key="all" /> : null}
      {tabs.map(tag => <TabPane key={tag.value} tab={`${tag.label}`} />)}
    </Tabs>
  </FlexItem>
}

export default withRouter(ActionOnTabs);