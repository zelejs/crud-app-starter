import React, { useState, useEffect } from 'react';
import { ChakraProvider, VStack, HStack, Box, Button, Text, FormControl, FormLabel } from '@chakra-ui/react';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import LocalPreview from '@/composition/localPreview';
import AddCarts from '@/composition/AddCarts'
import EditProps from '@/composition/editProps';


export default function Index(props) {

  const { id, status, isSwitch } = props;
  const api1 = '/openapi/lc/module?componentOption=cart&pageNum=1&pageSize=100'
  const layoutApi1 = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/129'

  const [api, setApi] = useState(api1)
  const [layoutApi, setLayoutApi] = useState(layoutApi1)
  const [previewData, setPreviewData] = useState('')

  const [isAddClick, setIsAddClick] = useState(false)
  const [moduleName, setModuleName] = useState('')

  useEffect(() => {
    setApi(api1)
    setLayoutApi(layoutApi1)
  },[api, layoutApi])

  //左侧cart列表 item点击事件
  const onComponentItemClick = (item) => {
    setIsAddClick(false)
    if (item.isSelected) {
      setModuleName(item.moduleName)
      setPreviewData({
        __cart2: {
          xname: item.componentType,
          props: item.componentProps
        }
      })
    }
  }

  //新增cart按钮
  const addNewClick = () => {
    setIsAddClick(!isAddClick)
  }
  //新增cart回调事件
  const cb = (status) => {
    if (status === 'success') {
      setApi()
      setLayoutApi()
      setIsAddClick(false)
      setTimeout(() => {
        setApi(api1)
        setLayoutApi(layoutApi1)
      }, 100)
    } else if (status === 'error') {
      setIsAddClick(false)
    }
  }

  const onCompleted = (data) => {
    setApi()
    setLayoutApi()
  }

  return (
    <ChakraProvider>
      <VStack align='stretch' spacing='-2'>

        <HStack spacing={'0'}>
          <Box style={{ height: window.innerHeight, padding: '8px', background: '#fff' }}>
            {
              api && layoutApi ? (
                <PreviewAutoLayout layoutApi={layoutApi} api={api} onItemClick={onComponentItemClick} onAddNewClick={addNewClick} isSwitch={isSwitch} />
              ) : <></>
            }
          </Box>

          {
            isSwitch && isAddClick ? (
              <>
                <Box style={{ width: '6px', height: window.innerHeight }} background={'#EDECF1'}></Box>
                <Box style={{ width: '100%', height: window.innerHeight, padding: '8px' }} background={'#fff'}>
                  <AddCarts cb={cb} />
                </Box>
              </>
            ) : (
              <Box style={{ width: '100%', height: window.innerHeight, padding: '8px' }} background={'#EDECF1'}>
                {
                  previewData ? (
                    <VStack spacing={2} alignItems={'flex-start'} >
                      <EditProps moduleName={moduleName} onActionCompleted={onCompleted} />
                      <LocalPreview previewData={previewData} type='cart' />
                    </VStack>
                  ) : <></>
                }
              </Box>
            )
          }
        </HStack>
      </VStack>
    </ChakraProvider >
  )

}