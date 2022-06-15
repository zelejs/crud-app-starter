import React, { useReducer, useState, useEffect } from 'react';
import PrimaryLayout from '@/framework/PrimaryLayout';
import GlobalContext from '@/framework/GlobalContext';
import window from 'zero-element/lib/utils/window';
import { BackTop } from 'antd';

import router from '@/config/router.config';
import profileMenuData from '@/config/profile.config';
import router401Data from '@/config/401.config';

let menuData = [...router];

function reducer(state, { type, payload }) {
  const method = {
    save() {
      return {
        ...state,
        ...payload,
      }
    },
    clearBc() {
      return {
        ...state,
        breadcrumb: [],
      }
    },
    defaults() {
      console.warn(`未定义的方法: ${type}`);
      return state;
    }
  };
  return (method[type] || method['defaults'])();
};

function BasicLayout(props) {
  const { location } = props;
  const { pathname } = location;

  const [state, dispatch] = useReducer(reducer, {
    breadcrumb: [],
    style: {
      nav: window.ZEle.nav,
      theme: window.ZEle.theme,
    },
    OnBreadcrumb: handleBreadcrumb,
    OnBreadcrumbClear: handleBreadcrumbClear,
  });

  function handleBreadcrumb(breadcrumb) {
    dispatch({
      type: 'save',
      payload: {
        breadcrumb,
      },
    });
  }
  function handleBreadcrumbClear() {
    dispatch({
      type: 'clearBc',
    });
  }

  return (
    <GlobalContext.Provider value={state}>
      <BackTop
        target={_ => document.getElementById('contentContainer')}
      />
      <PrimaryLayout
        {...props}
        breadcrumb={state.breadcrumb}
        menuData={switchMenuData(pathname, menuData)}
      >
      </PrimaryLayout>
    </GlobalContext.Provider>
  );
}

const reg = /^\/profile\//;
const reg3 = /^\/401\//;
function switchMenuData(pathname, menuData) {
  
  if (reg.test(pathname)) {
    return profileMenuData;
  }

  if(reg3.test(pathname)){
    return router401Data;
  }

  return menuData;

}

export default BasicLayout;
