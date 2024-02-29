import React, { useState } from 'react';
import { VStack, Box, Button, HStack } from '@chakra-ui/react';
import { history } from 'umi';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');
import LocalPreview from '@/composition/localPreview';

const routeMap = {
  presenter: '/presenters',
  cart: '/carts',
  layout: '/layouts',
  container: '/containers',
}

export default function Index(props) {

  const { id } = props
  const api = '/openapi/lc/module?componentOption=selector'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/163'

  const [ previewData, setPreviewData ] = useState('')

  //保存数据
  function saveData(item) {
    let api = '/openapi/lc/module/build-auto-layout/' + id
    const queryData = {
      addModuleId: item.id,
    };
    promiseAjax(api, queryData, { method: 'PATCH' }).then(resp => {
      setLoading(false)
      if (resp && resp.code === 200) {
        goViewPage()
      } else {
        console.error("添加selector失败 = ", resp)
        toastTips("添加失败")
      }
    });
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

    if (item.isSelected) {
        setPreviewData({
          __selector2:{
            xname: item.componentType,
            props: item.componentProps
          }
        })
    }
  }


  return (
      <VStack align='stretch' spacing='-2'>
        {/* <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
          <Button colorScheme='teal' size='sm' marginRight={'8px'} onClick={() => goViewPage()}>
            返回
          </Button>
        </Box> */}

        <HStack spacing={'0'}>
          <Box style={{ height: window.innerHeight, padding: '8px', background: '#fff'}}>
            <PreviewAutoLayout layoutApi={layoutApi} api={api} onItemClick={onComponentItemClick} />
          </Box>

          <Box style={{ width: '100%', height: window.innerHeight, padding: '8px' }} background={'#EDECF1'}>
            {
              previewData ? (
                <LocalPreview previewData={previewData} />
              ) : <></>
            }
          </Box>
        </HStack>

      </VStack>
  )

}