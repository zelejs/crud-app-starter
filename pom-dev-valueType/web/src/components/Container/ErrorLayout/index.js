import React from 'react';

import { Row, Button } from 'antd';

import './index.less';

export default function ErrorLayout(props) {

  const { config } = props;
  const { status } = config;
  
  return <div className='errorBody'>

    <Row align='center' justify='center'>
        <img  src='error_img.png'/>
        <div className='errorContent'>
            <div className='title textItem'>{status}</div>
            <div className='subTitle textItem'>抱歉, 你无法访问该页面</div>
            <div className='btn textItem'>
              <Button type="primary" href='/login'>转至登录页</Button>
            </div>
        </div>
    </Row>

  </div>
}