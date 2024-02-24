import React, { useState } from 'react';
import { ChakraProvider, VStack, Box, Button, HStack } from '@chakra-ui/react';
import { history } from 'umi';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import LocalPreview from '@/composition/localPreview';

const promiseAjax = require('zero-element-boot/lib/components/utils/request');
const routeMap = {
  presenter: '/presenters',
  cart: '/carts',
  layout: '/layouts',
  container: '/containers',
  finish: '/view'
}

export default function Index (props) {

  const { id, status } = props
  const api = '/openapi/lc/module?componentOption=layout'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/153'
    
  const [ previewData, setPreviewData ] = useState('')

  //保存数据
  function saveData(itemData) {
    let api = '/openapi/lc/module/build-auto-layout/' + id
    const queryData = {
        addModuleId: itemData.id
    };
    promiseAjax(api, queryData, {method: 'PATCH'}).then(resp => {
        if (resp && resp.code === 200) {
            toPage(resp.data.nextComponent)
        } else {
            console.error("添加layout失败 = ", resp)
            toastTips("添加失败")
        }
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
          console.error("更换layout失败 = ", resp)
          toastTips(resp.message)
      }
      }).finally(_ => {
      // setLoading(false)
      });
  }

  //获取应该跳转到哪一页
  function getNextDataToPage(itemData) {
    let api = '/openapi/lc/module/build-auto-layout/'+id
    const queryData = {
        skipComponentOptionList: ["cart"]
    };
    promiseAjax(api, queryData, {method: 'PATCH'}).then(resp => {
        // setLoading(false)
        if (resp && resp.code === 200) {
            const skipList = ["cart"]
            skipList.push(resp.data.nextComponent)
            localStorage.setItem('skipComponentOptionList', JSON.stringify(skipList))
            toPage(resp.data.nextComponent)
        } else {
            console.error("跳过失败 = ", resp)
            toastTips("跳过失败")
        }
    });
  }

  //返回详情页
  function goViewPage(){
    history.push({
        pathname: '/view',
        query: {
            id
        }
    })
  }

  //跳过单前页, 进入下一步
  const nextPage = () => {
    getNextDataToPage()
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

  const onComponentItemClick = (item) => {
    if (item.isSelected) {
        setPreviewData({
            __layout2:{
                xname: item.componentType,
                props: item.componentProps
            }
        })
}
  }

  return (
    <ChakraProvider>
        <VStack align='stretch' spacing='-2'>
            {/* <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
                    <Button colorScheme='teal' size='sm' marginRight={'8px'} onClick={() => goViewPage()}>
                        返回
                    </Button>
                    { status != 'edit' && (
                        <Button colorScheme='teal' size='sm' onClick={() => nextPage()}>
                            跳过
                        </Button>
                    ) }
            </Box> */}
            <HStack spacing={0}>
                <Box style={{ width: '550px', height: '100vh', padding: '8px', background: '#fff' }}>
                <PreviewAutoLayout  layoutApi={layoutApi} api={api} onItemClick={onComponentItemClick} />
                </Box>

                <Box style={{ width: '100%', height: '100vh', padding: '8px' }} background={'#EDECF1'}>
                {
                    previewData ? (
                        <LocalPreview previewData={previewData} type='layout' />
                    ) : <></>
                }
                </Box>
            </HStack>

        </VStack>
    </ChakraProvider>
  )

}