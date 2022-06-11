import React from 'react';
import Flex from '@/../zero-antd-dep/layout/Flex';
import { Button } from 'antd';
import {history} from 'umi'
import './index.less';

const { FlexItem } = Flex;

export default function ({ title,isBack, children }) {
  const [child,extra] = React.Children.toArray(children);

  function back(){
    history.goBack(1)
  }

  return <div className="c-Card">
    <Flex
      className="content"
    >
      <FlexItem>
        <div className="title">{title}{isBack?<Button style={{position:"absolute",right:"10px",top:"10px"}} onClick={back}>返回</Button>:null}
        </div>
      </FlexItem>
      <div className="fill"></div>
      <FlexItem flex={1}>
        {extra}
      </FlexItem>
    </Flex>
    {child}
  </div>
}