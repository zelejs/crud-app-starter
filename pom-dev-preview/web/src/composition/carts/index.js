import React, { useState } from 'react';
import { ChakraProvider, VStack, HStack, Box, Button, Text, FormControl, FormLabel } from '@chakra-ui/react';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import LocalPreview from '@/composition/localPreview';
import AddCarts from '@/composition/AddCarts'


export default function Index(props) {

  const { id, status, isSwitch } = props;
  const api = '/openapi/lc/module?componentOption=cart'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/129'

  const [previewData, setPreviewData] = useState('')

  const [isAddClick, setIsAddClick] = useState(false)

  //左侧cart列表 item点击事件
  const onComponentItemClick = (item) => {
    setIsAddClick(false)
    if (item.isSelected) {
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
    console.log('cb status = ', status)
  }

  return (
    <ChakraProvider>
      <VStack align='stretch' spacing='-2'>

        <HStack spacing={'0'}>
          <Box style={{ height: '100vh', padding: '8px', background: '#fff' }}>
            <PreviewAutoLayout layoutApi={layoutApi} api={api} onItemClick={onComponentItemClick} onAddNewClick={addNewClick} isSwitch={isSwitch} />
          </Box>

          {
            isSwitch && isAddClick ? (
              <>
                <Box style={{ width: '8px', height: '100vh' }} background={'#EDECF1'}></Box>
                <VStack style={{ width: '100%', height: '100vh', padding: '8px' }} background={'#fff'}>
                  <HStack spacing={5} w={'100%'} justifyContent={'space-between'} p={'0 50px'}>
                    <Button colorScheme='teal' size='sm' onClick={() => { }}>
                      返回
                    </Button>
                    <Text fontSize={'16px'} fontWeight={'bold'}>新增组件</Text>
                    <Button colorScheme='teal' size='sm' onClick={() => { }}>
                      跳过
                    </Button>
                  </HStack>
                  <Box style={{ width: '100%', height: '100vh' }}>
                    <AddCarts cb={cb} />
                  </Box>
                </VStack>
              </>
            ) : (
              <Box style={{ width: '100%', height: '100vh', padding: '8px' }} background={'#EDECF1'}>
                {
                  previewData ? (
                    <LocalPreview previewData={previewData} type='cart' />
                  ) : <></>
                }
              </Box>
            )
          }
        </HStack>
      </VStack>
    </ChakraProvider>
  )

}