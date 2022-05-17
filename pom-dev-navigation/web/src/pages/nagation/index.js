import React, { useState, useEffect } from 'react';
import { ChakraProvider, Box, VStack, Spinner, Switch, FormControl, FormLabel, } from "@chakra-ui/react";
import { getEndpoint } from 'zero-element-boot/src/components/config/common';
import { history } from 'umi';
import { AutoLayout } from 'zero-element-boot';
// const promiseAjax = require('@/components/utils/request');
import layout from '../nagation/layout'
// import layout from './Standalone/layout';
import { Page } from 'zero-element-boot/lib/components/cart'
import TabsCompox from 'zero-element-boot/lib/composition/testCrudList/compx/tabsComps'
const promiseAjax = require('zero-element-boot/lib/components/utils/request');
// import { setEndpoint, setToken } from 'zero-element-boot/lib/components/config/common';
export default function index (props) {

  const { } = props;


  const [listData, setListData] = useState([])
  const [isLoading, setLoading] = useState(false)
  const [switchStatus, setSwitchStatus] = useState(false)
  const [navCateListData, setNavCateListData] = useState([]);
  const [tabIndex, setTabIndex] = useState(0);


  let api = '/api/pub/data/services/navigation';
  let navListApi = '/api/pub/data/services/navigation';
  let navApi = '/api/pub/data/services/navCategory';
  useEffect(() => {
    console.log('首次加载')
    const queryData = {}
    fetchData(api, queryData)
    fetchNavCategoryData(navApi, {});
  }, []);

  useEffect(() => {

  }, []);

  let layoutData = '';
  const layoutJsonPath = '';
  const localLayoutJson = layout;

  if (layoutJsonPath) {
    layoutData = { path: layoutJsonPath };
  } else {
    layoutData = localLayoutJson;
  }
  const config = {
    items: listData,
    layout: layoutData
  };


  //获取分类列表信息

  const fetchNavCategoryData = (api, queryData) => {
    setLoading(true);
    let newNavCateList = [];
    return promiseAjax(api, queryData).then(resp => {
      if (resp && resp.code === 200) {
        newNavCateList = resp.data.records; //-1:新增  -2删除

        newNavCateList.push({
          id: '-1'
        });
        newNavCateList.push({
          id: '-2'
        });
        setNavCateListData(newNavCateList);
        setLoading(false);
      } else {
        console.error('获取列表数据失败 ==', resp);
      }
    }).finally(_ => {
      setLoading(false);
      setTabIndex(newNavCateList[0].id);
      fetchData(navListApi, {
        typeId: newNavCateList[0].id
      });
    });
  };
  // </Page>
  //获取列表信息
  const fetchData = (api, queryData) => {
    setLoading(true)
    return promiseAjax(api, queryData).then(resp => {
      if (resp && resp.code === 200) {
        const list = resp.data.records;
        setListData(list);
        setLoading(false)
      } else {
        console.error('获取列表数据失败 ==', resp)
      }
    });
  }

  //
  const onUserItemClick = (item) => {
    const id = item.id;
    console.log('id = ', id)
    // alert(`选择的用户id为: ${id}`)
    //点击跳转页面
    if (item.path.indexOf('http') != -1) {
      // window.location.replace(item.path)

      // history.push(url);
      const w = window.open('about:blank');
      w.location.href = item.path
      console.log(item.path);

    } else {
      const w = window.open('about:blank');
      const host = getEndpoint || location.host
      w.location.href = host + item.path

      console.log(host);

    }

  }

  //回调函数
  const callback = (value) => {

    console.log('item1111111 = ', value)
    if (value) {
      fetchData(api, {})
    }
  }



  const tabscallback = value => {
    if (value) {
      setNavCateListData([]);
      setListData([]);
      fetchNavCategoryData(navApi, {});
    }
  }; //开启/关闭 编辑按钮




  const handleChange = () => {
    const status = !switchStatus;
    setSwitchStatus(status)
    if (!status) {
      setNavCateListData([]);
      setListData([]);
      fetchNavCategoryData(navApi, {});
    }
  }



  //tab切换
  const switchTab = (item, index) => {
    if (index != tabIndex) {
      setTabIndex(index)
      const queryData = {
        typeId: item.id
      }
      fetchData(navListApi, queryData)
    }
  }
  return (
    <Page >

      <ChakraProvider>
        <div style={{ maxWidth: '800px' }}>
          <VStack align='stretch' spacing='-2'>
            <Box style={{ margin: '10px 10px 30px 10px', paddingLeft: '8px' }}>
              <FormControl display='flex' alignItems='center'>
                <FormLabel htmlFor='email-alerts' mb='0'>
                  编辑开关：
                </FormLabel>
                <Switch size='lg' onChange={() => handleChange()} isChecked={switchStatus} />
              </FormControl>
            </Box>
            <Box>
            </Box>
            {navCateListData && navCateListData.length > 0 ? (
              <>
                <TabsCompox items={navCateListData} onSwitchTab={switchTab} isSwtich={switchStatus} cb={tabscallback} />

                <div style={{ marginTop: '20px' }}>
                  {isLoading ? (
                    <Spinner />
                  ) : (
                    <Box>
                      <AutoLayout {...config} onItemClick={onUserItemClick} cb={callback} isSwtich={switchStatus} />
                    </Box>
                  )
                  }
                </div>
              </>
            ) : null}

          </VStack>

        </div>

      </ChakraProvider>

    </Page >

  )

}
