import React, { useState } from 'react';
import { VStack, HStack, Box } from '@chakra-ui/react';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import LocalPreview from '@/composition/localPreview/view';


export default function Index (props) {

  const { id, status } = props.location && (props.location.query ||  qs.parse(props.location.search.split('?')[1])) 
  const api = '/openapi/lc/module?componentOption=cart'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/129'

  const [previewLayout, setPreviewLayout] = useState('')

  const onComponentItemClick = (item) => {
    if (item.isSelected) {

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
            previewLayout ? (
              <LocalPreview previewLayout={previewLayout} />
            ) : <></>
          }
        </Box>
      </HStack>
    </VStack>
  )

}