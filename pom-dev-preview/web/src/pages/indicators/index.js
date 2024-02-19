import React, { useState } from 'react';
import { ChakraProvider, VStack, HStack, Box  } from '@chakra-ui/react';
import { history } from 'umi';
import Title from 'zero-element-boot/lib/components/presenter/Title';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import localPreviewLayoutConfig from '@/composition/localPreview/layout.json';
import LocalPreview from '@/composition/localPreview';

export default function Index (props) {

  const { id } = props.location && (props.location.query ||  qs.parse(props.location.search.split('?')[1])) 
  const api = '/openapi/lc/module?componentOption=indicator'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/161'

  const [ previewLayout, setPreviewLayout ] = useState('')


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
    if(item.isSelected){
      console.log(item)
      const iconLayoutConfig = {
        ...localPreviewLayoutConfig,
        presenter: {
          xname: 'ItemPlaceholder',
          props: {
              bg: '#FEFCFE'
          }
        },
        indicator:{
          xname: item.componentType,
          props: item.componentProps
        }
      }

      const textLayoutConfig = {
        ...localPreviewLayoutConfig,
        presenter: {
          xname: 'Text',
          props: {
          }
        },
        indicator:{
          xname: item.componentType,
          props: item.componentProps
        }
      }
      setPreviewLayout({textLayoutConfig, iconLayoutConfig})
    }
  }

  return (
    <ChakraProvider>
      <VStack align='stretch' spacing='-2'>
          {/* <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
              <Button colorScheme='teal' size='sm' marginRight={'8px'} onClick={() => goViewPage()}>
                  返回
              </Button>
          </Box> */}

          <HStack spacing={'1'}>

            <Box style={{ width:'400px', height: '100vh', padding: '8px' }}>
                <PreviewAutoLayout  layoutApi={layoutApi} api={api} onPreviewItemClick={onComponentItemClick} />
            </Box>

            <Box style={{ width:'1000px', height: '100vh', padding: '8px' }}>
              <VStack>
                <Box w={'100%'} marginBottom={'10px'}>
                  <VStack>
                    <Box w={'100%'} marginBottom={'6px'}>
                      <Title content="图片" />
                    </Box>
                    <Box w={'100%'} marginLeft={'1px'}>
                      { previewLayout && <LocalPreview configLayout={previewLayout.iconLayoutConfig} />} 
                      
                    </Box>
                  </VStack>
                </Box>
                <Box w={'100%'}>
                  <VStack>
                    <Box w={'100%'} marginBottom={'6px'}>
                      <Title content="文字" />
                    </Box>
                    <Box w={'100%'}>
                      { previewLayout && <LocalPreview configLayout={previewLayout.textLayoutConfig} />}
                    </Box>
                  </VStack>
                </Box>
              </VStack>
            </Box>

        </HStack>

        </VStack>

        
    </ChakraProvider>
  )

}