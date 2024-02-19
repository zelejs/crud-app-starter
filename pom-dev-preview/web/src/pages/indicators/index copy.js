import React from 'react';
import { ChakraProvider, VStack, Box, Button  } from '@chakra-ui/react';
import { history } from 'umi';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';

const routeMap = {
  presenter: '/presenters',
  cart: '/carts',
  layout: '/layouts',
  container: '/containers',
}

export default function Index (props) {

  const { id } = props.location && (props.location.query ||  qs.parse(props.location.search.split('?')[1])) 
  const api = '/openapi/lc/module?componentOption=indicator'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/161'


  //保存数据
  function saveData(itemData) {
    let api = '/openapi/lc/module/build-auto-layout/'+id
    const queryData = {
        addModuleId:itemData.id,
    };
    promiseAjax(api, queryData, {method: 'PATCH'}).then(resp => {
        setLoading(false)
        if (resp && resp.code === 200) {
            goViewPage()
        } else {
            console.error("添加container失败 = ", resp)
            toastTips("添加失败")
        }
    });
  }


  //返回详情页
  function goViewPage(){
      history.push({
          pathname: '/view',
          query:{
              id
          }
      })
  }

  const onComponentItemClick = (item) => {
    if(itemData.isSelected){
      saveData(itemData)
    }
  }


  return (
    <ChakraProvider>
      <VStack align='stretch' spacing='-2'>
          <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
              <Button colorScheme='teal' size='sm' marginRight={'8px'} onClick={() => goViewPage()}>
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