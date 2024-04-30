import React, { useState, useEffect} from 'react';
import { VStack, HStack, Box } from '@chakra-ui/react';
import { history } from 'umi';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import LocalPreview from '@/composition/localPreview';
import EditProps from '@/composition/editProps';

export default function Index(props) {

  const { id } = props
  const api1 = '/openapi/lc/module?componentOption=indicator'
  const layoutApi1 = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/161'

  const [api, setApi] = useState(api1)
  const [layoutApi, setLayoutApi] = useState(layoutApi1)
  const [ previewData, setPreviewData ] = useState('')
  const [moduleName, setModuleName] = useState('')

  
  useEffect(() => {
    setApi(api1)
    setLayoutApi(layoutApi1)
  },[api, layoutApi])

  const onComponentItemClick = (item) => {
    if (item.isSelected) {
      setModuleName(item.moduleName)
      setPreviewData({
        __indicator2:{
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
            ):<></>
          }
          
        </Box>

        <Box style={{ width: '100%', height: window.innerHeight, padding: '8px' }} background={'#EDECF1'}>
    
          {
            previewData ? (
              <VStack spacing={2} alignItems={'flex-start'} >
                <EditProps moduleName={moduleName} onActionCompleted={onCompleted} />
                <LocalPreview previewData={previewData} type='indicator' />
              </VStack>
            ) : <></>
          }
        </Box>
      </HStack>
    </VStack>
  )

}