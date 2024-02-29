import React, { useState } from 'react';
import { VStack, HStack, Box } from '@chakra-ui/react';
import { history } from 'umi';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import LocalPreview from '@/composition/localPreview';

export default function Index(props) {

  const { id } = props
  const api = '/openapi/lc/module?componentOption=indicator'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/161'

  const [ previewData, setPreviewData ] = useState('')


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
        __indicator2:{
          xname: item.componentType,
          props: item.componentProps
        }
      })
    }
  }

  return (
    <VStack align='stretch' spacing='-2'>
      <HStack spacing={'0'}>
        <Box style={{ height: window.innerHeight, padding: '8px', background: '#fff' }}>
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