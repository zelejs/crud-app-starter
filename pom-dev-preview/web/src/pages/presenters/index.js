import React, {useState, useEffect} from 'react';
import { ChakraProvider, VStack, Box, Button } from '@chakra-ui/react';
import { history } from 'umi';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');
const routeMap = {
  presenter: '/presenters',
  cart: '/carts',
  layout: '/layouts',
  container: '/containers',
  finish: '/view'
}

export default function Index(props) {

  const { id, status } = props.location && (props.location.query || qs.parse(props.location.search.split('?')[1]))
  const api = '/openapi/lc/module?componentOption=presenter'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/160'

  // const [ loading, setLoading ] = useState(false);

  //保存数据
  function saveData(itemData) {
    let api = '/openapi/lc/module/build-auto-layout/' + id
    const queryData = {
      addModuleId: itemData.id
    };
    promiseAjax(api, queryData, { method: 'PATCH' }).then(resp => {
      if (resp && resp.code === 200) {
        toPage(resp.data.nextComponent)
      } else {
        console.error("添加presenter失败 = ", resp)
        toastTips(resp.message)
      }
    }).finally(_ => {
      // setLoading(false)
    });
  }

  //更换
  function editData(itemData) {
    let api = '/openapi/lc/module/AutoLayout/replaceModule/' + id
    const queryData = {
      replaceModuleId:itemData.id
    };
    promiseAjax(api, queryData, { method: 'PUT' }).then(resp => {
      if (resp && resp.code === 200) {
        goViewPage()
      } else {
        console.error("更换presenter失败 = ", resp)
        toastTips(resp.message)
      }
    }).finally(_ => {
      // setLoading(false)
    });
  }

  const toPage = (nextComponent) => {
    const path = routeMap[nextComponent]
    history.push({
      pathname: path,
      query: {
        id
      }
    })
  }

  //返回详情页
  function goViewPage() {
    history.push({
      pathname: '/view',
      query: {
        id
      }
    })
  }
  const onComponentItemClick = (item) => {
    // console.log('item = ', item)
    if (item.isSelected) {
      if(status === 'edit'){
        editData(item)
      }else{
        saveData(item)
      }
    }
  }
  return (
    <ChakraProvider>
      <VStack align='stretch' spacing='-2'>
          <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
                  <Button colorScheme='teal' size='sm' onClick={() => goViewPage()}>
                      返回
                  </Button>
          </Box>

          <Box style={{ margin: '5px 5px 5px 5px', paddingLeft: '8px' }} >
            <PreviewAutoLayout  layoutApi={layoutApi} api={api} onPreviewItemClick={onComponentItemClick} />
          </Box>
        </VStack>
    </ChakraProvider>
  )

}