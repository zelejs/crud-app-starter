import React, { useState,useEffect } from 'react';
import { history } from 'umi';
import { removeToken, getUserName, getExtra, getAvatar,setAvatar, getToken } from 'zero-element/lib/utils/request/token';
import { Avatar, Menu, Dropdown,Tooltip } from 'antd';
import { get as getEndpoint } from 'zero-element/lib/utils/request/endpoint';
import {
  BellOutlined,
  UserOutlined,
  AppstoreOutlined,
  LogoutOutlined,
  SwapOutlined,
} from '@ant-design/icons';

// import { TestUserSelection } from 'zero-element-plugins';

import { LS } from 'zero-element/lib/utils/storage';

import { ConfigSvg, DocSvg, MoreSvg, TableSvg,WorkflowSvg } from './svg/Svg'

import './index.less'



function openToDoListPage() {
  history.push('/toDoList');
}


export default (props) => {

  const [visible, useVisible] = useState(false);
  const [uVisible, useUVisible] = useState(false);

  function handleVisibleChange(visible) {
    useVisible(visible);
    if (!visible) {
      useUVisible(false)
    }
  }

  function handleOnItemClickHandle() {
    useVisible(false);
    useUVisible(false);
  }

  function handleLogOut() {
    LS.del('menuList');
    useVisible(false);
    removeToken();
    history.push('/login');
  }

  function handleRouteTorofile() {
    history.push('/profile/userpage');
    useVisible(false);
  }


  function ConfigClick(){
    history.push('/designpage/config');
    useVisible(false);
  }

  // console.log(Random);
  let endpoint= getEndpoint()
  
  // const API = `${endpoint}/api/crud/avatar/${Random}`
  const API = `${endpoint}/api/adm/users/userInfo`
  let token = getToken()
  useEffect(() => {
        
    // fetch(API,{
    //     headers:{
    //         "Content-Type":"application/json;charset=UTF-8",
    //         "Authorization":`Bearer ${token}`
    //     }
    // })
    // .then(res=>{
    //     let json=res.json();
    //     console.log(json);
    //     Promise.resolve(json).then((val)=>{
    //         // console.log(val.data.avatar)
    //         if(val.code===200){
    //           setAvatar(endpoint+val.data.avatar)
    //           // setAvatar(endpoint+val.data)
    //           console.log(Avatar);
    //         }else{
    //           console.error("错误")
    //         }
    //     })

    // })
    // .catch(err=>{
    //     console.log(err);
    // })
},[])
  

  const menu = (
    <Menu>
      {/* <Menu.Item disabled>
        <div style={{ textAlign: 'center', cursor: 'default', color: '#666' }}>{getUserName()}</div>
      </Menu.Item> */}
      <Menu.Item style={{ width: 200, height: 180, position: "relative", cursor: 'default' }}>
        <Avatar src={getAvatar()} style={{ position: 'relative', left: '50%', right: 35, top:"-10px", transform: ' translate(-50%)' ,background:"white"}} icon={<UserOutlined style={{ fontSize: 60, paddingright: 8, paddingLeft: 5 }} />} size={90} />
        <span style={{ fontSize: 17, textAlign: 'center', fontWeight: 'bolder', position: 'absolute', bottom: 20, left: '50%', transform: ' translate(-50%)' }}>{getUserName()}</span>
      </Menu.Item>
      {/* <Menu.Item onClick={handleRouteTorofile}>
        <AppstoreOutlined style={{ paddingLeft: 10 }} />
        <span className="ZEleA-margin-left">个人中心</span>
      </Menu.Item> */}
      <Menu.Divider />
      {process.env.NODE_ENV === 'development' ? (
        <>
          <Menu.Item onClick={_ => useUVisible(true)}>
            <SwapOutlined style={{ paddingLeft: 10 }} />
            <span className="ZEleA-margin-left">切换账号</span>
          </Menu.Item>
          <Menu.Divider />
        </>
      ) : null}
      <Menu.Item onClick={handleLogOut}>
        <LogoutOutlined style={{ paddingLeft: 10 }} />
        <span className="ZEleA-margin-left">退出账号</span>
      </Menu.Item>

    </Menu>
  );

  const messageMenu = (
    <Menu>
      <Menu.Item key="1" onClick={_ => openToDoListPage(true)}>
        待办事项
      </Menu.Item>
    </Menu>
  )

  const MoreMenu = (
    <Menu>
      <Menu.Item key="More1">
        <a href="/designpage/design">设计</a>
      </Menu.Item>
    </Menu>
  )

const formMenu = (
  <Menu>
  <Menu.Item key="More1">
    <a href="/formItemTypeManage">表单组件</a>
    </Menu.Item>
  </Menu>
)

  const userMenu = (
    <Menu>
      <Menu.Item style={{ padding: 0 }} key="userMenu">
        测试用户
        {/* <TestUserSelection onItemClickHandle={() => handleOnItemClickHandle()} /> */}
      </Menu.Item>
    </Menu>
  )

  // 右上角按钮
  const RightButton = (
    <>
        <Tooltip title="前往流程设计" placement="bottom">
          <a a className="Svg" href="https://r.zbsoft.top/sport" style={{height: "60px",lineHeight: "80px",width:"32px",marginRight:"10px"}}><WorkflowSvg width="30" height="30"/></a>
        </Tooltip>
        <Tooltip title="表单组件展示" placement="bottom">
          <Dropdown
          trigger={['click']}
          placement="bottomLeft"
          overlay={formMenu}
        >
          <a className="Svg" style={{height: "60px",lineHeight: "80px",width:"32px",marginRight:"10px"}}><TableSvg width="30" height="30"/></a>
        </Dropdown>
        </Tooltip>
      <Tooltip title="系统设置" placement="bottom">
        <a className="Svg" onClick={ConfigClick} style={{height: "60px",lineHeight: "80px",width:"32px",marginRight:"10px"}}><ConfigSvg width="30" height="30"/></a>
      </Tooltip>
      <Tooltip title="设计" placement="bottom">
      <Dropdown
        trigger={['click']}
        placement="bottomLeft"
        overlay={MoreMenu}
      >
      <a className="Svg" href="/Tag/TagView" style={{height: "60px",lineHeight: "80px",width:"32px",marginRight:"10px"}}><MoreSvg width="30" height="30"/></a>
        {/* <a href="Tag/TagView" className="RightNav more"></a> */}
      </Dropdown>
      </Tooltip>

      {/* <a href="http://docs.smallsaas.cn" className="RightNav docs"></a> */}
      <Tooltip title="文档" placement="bottom">
        <a className="Svg" href="http://docs.smallsaas.cn" style={{height: "60px",lineHeight: "80px",width:"32px",marginRight:"10px"}}><DocSvg width="30" height="30"/></a>
      </Tooltip>
      <Tooltip title="待办" placement="bottom">
        <Dropdown
          trigger={['click']}
          placement="bottomLeft"
          overlay={messageMenu}
        >
          <BellOutlined style={{ fontSize: 25, paddingRight: 20 }} />
        </Dropdown>
      </Tooltip>
    </>
  )

  
  return (
    <div style={{ 'display': 'flex', 'flexDirection': 'row', 'alignItems': 'center' }}>
      {/* {RightButton} */}
      <Dropdown
        placement="bottomRight"
        trigger={['click']}
        visible={visible}
        onVisibleChange={handleVisibleChange}
        overlay={uVisible ? userMenu : visible ? menu : <></>}
        >
        <div>
          {/* <span style={{ fontSize: 22 }}>|</span> */}
          {/* <span style={{ paddingRight: 15, paddingLeft: 15, fontSize: 17 }}>{getUserName()}</span> */}
          {/* <span style={{ fontSize: 0 }}>{getExtra()}</span> */}
          {/* <Avatar src={getAvatar()} style={{background:"white"}} icon={<UserOutlined style={{ fontSize: 24 }} />} size={36} /> */}
        </div>
      </Dropdown>
    </div>
  )

}