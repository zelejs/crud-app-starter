import React, { useState, useEffect, useRef, useReducer, useMemo } from 'react';
import Flex from '@/../zero-antd-dep/layout/Flex';
import { DownOutlined } from '@ant-design/icons';
import './index.css';

const { FlexItem } = Flex;

export default function Panel({
  title,
  defaultCollapse = false,
  collapseIcon = true,
  delayed = false,
  children,
}) {
  const [collapse, setCollapse] = useState(defaultCollapse);
  const [count, forcedUpdate] = useReducer(x => x + 1, 0);
  const domContent = useRef(null);
  const height = useRef(undefined);

  const [contentStyle, iconStyle] = useMemo(_ => {
    if (collapse) {
      return [
        { height: 0, padding: 0 },
        { transform: 'rotate(180deg)' },
      ]
    }
    return [
      { height: height.current, padding: undefined },
      { transform: undefined },
    ];
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [collapse, count]);

  useEffect(_ => {
    if (!collapse) {
      setTimeout(_ => {
        height.current = undefined;
        forcedUpdate();
      }, 200)
    }
  }, [collapse]);

  function handleCollapse() {
    if (!collapse) {
      height.current = domContent.current.clientHeight;
    }
    setCollapse(!collapse);
  }

  return <div className="ZEleA-Panel">
    <Flex className="ZEleA-Panel-title">
      {collapseIcon ? (
        <FlexItem className="ZEleA-Panel-icon" onClick={handleCollapse} style={iconStyle}>
          <DownOutlined type="down" />
        </FlexItem>
      ) : null}
      <FlexItem flex={1}>
        <div>
          {title}
        </div>
      </FlexItem>
    </Flex>
    <div className="ZEleA-Panel-content"
      ref={domContent}
      style={contentStyle}
    >
      {delayed ? (
        !collapse && children
      ) : children}
    </div>
  </div>
}