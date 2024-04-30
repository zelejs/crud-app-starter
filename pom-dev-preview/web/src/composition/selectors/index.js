import React, { useState, useEffect } from 'react';
import { VStack, Box, Button, HStack } from '@chakra-ui/react';
import { history } from 'umi';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');
import LocalPreview from '@/composition/localPreview';
import EditProps from '@/composition/editProps';

export default function Index(props) {

  const { id } = props
  const api1 = '/openapi/lc/module?componentOption=selector'
  const layoutApi1 = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/163'

  const [api, setApi] = useState(api1)
  const [layoutApi, setLayoutApi] = useState(layoutApi1)
  const [previewData, setPreviewData] = useState('')
  const [moduleName, setModuleName] = useState('')

  useEffect(() => {
    setApi(api1)
    setLayoutApi(layoutApi1)
  }, [api, layoutApi])

  // //保存数据
  // function saveData(item) {
  //   let api = '/openapi/lc/module/build-auto-layout/' + id
  //   const queryData = {
  //     addModuleId: item.id,
  //   };
  //   promiseAjax(api, queryData, { method: 'PATCH' }).then(resp => {
  //     setLoading(false)
  //     if (resp && resp.code === 200) {
  //       goViewPage()
  //     } else {
  //       console.error("添加selector失败 = ", resp)
  //       toastTips("添加失败")
  //     }
  //   });
  // }

  const onComponentItemClick = (item) => {

    if (item.isSelected) {
      setModuleName(item.moduleName)
      setPreviewData({
        __selector2: {
          xname: item.componentType,
          props: item.componentProps
        }
      })
    }
  }

  const onCompleted = (data) => {
    setApi()
    setLayoutApi()
  }

  return (
    <VStack align='stretch' spacing='-2'>

      <HStack spacing={'0'}>
        <Box style={{ height: window.innerHeight, padding: '8px', background: '#fff' }}>
          {
            api && layoutApi ? (
              <PreviewAutoLayout layoutApi={layoutApi} api={api} onItemClick={onComponentItemClick} />
            ) : <></>
          }
        </Box>

        <Box style={{ width: '100%', height: window.innerHeight, padding: '8px' }} background={'#EDECF1'}>

          {
            previewData ? (
              <VStack spacing={2} alignItems={'flex-start'} >
                <EditProps moduleName={moduleName} onActionCompleted={onCompleted} />
                <LocalPreview previewData={previewData} type='selector' />
              </VStack>
            ) : <></>
          }
        </Box>
      </HStack>

    </VStack>
  )

}