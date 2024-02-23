import React, { useState } from 'react';
import { VStack, HStack, Box } from '@chakra-ui/react';
import { history } from 'umi';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import LocalPreview from '@/composition/localPreview';

export default function Index(props) {

  const { id } = props.location && (props.location.query || qs.parse(props.location.search.split('?')[1]))
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
        <Box style={{ height: '100vh', padding: '8px', background: '#fff' }}>
          <PreviewAutoLayout layoutApi={layoutApi} api={api} onPreviewItemClick={onComponentItemClick} />
        </Box>

        <Box style={{ width: '100%', height: '100vh', padding: '8px' }} background={'#EDECF1'}>
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