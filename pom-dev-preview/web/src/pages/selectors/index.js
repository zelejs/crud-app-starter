import React, { useState } from 'react';
import { ChakraProvider, VStack, Box, Button, HStack } from '@chakra-ui/react';
import { history } from 'umi';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');
import LocalPreview from '@/composition/localPreview/view';

const routeMap = {
  presenter: '/presenters',
  cart: '/carts',
  layout: '/layouts',
  container: '/containers',
}

export default function Index(props) {

  const { id } = props.location && (props.location.query || qs.parse(props.location.search.split('?')[1]))
  const api = '/openapi/lc/module?componentOption=selector'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/163'

  const [previewLayout, setPreviewLayout] = useState('')

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
      const iconLayoutConfig = {
        presenter: {
          xname: 'DefaultAvatar',
          props: {
            size: 120
          }
        },
        selector: {
          xname: item.componentType,
          props: item.componentProps
        }, 
        "container": "SelectList",
      }

      const icon2LayoutConfig = {
        presenter: {
          xname: 'ImageSize',
          props: {
            width: 144,
            height: 'auto'
          }
        },
        selector: {
          xname: item.componentType,
          props: item.componentProps
        },
        "container": "SelectList",
      }

      const cardLayoutConfig = {
        presenter: {
          "children": [
            {
              "presenter": {
                xname: 'Text',
                props: {
                  fontSize: '18px',
                  color: '#fff',
                  marginBottom: '3px',
                },
              },
              "gateway": {
                  "xname": "Binding",
                  "props": {
                      "binding": {
                          "text": "content"
                      }
                  }
              }
            },
            {
                "presenter": {
                  xname: 'Text',
                  props: {
                    fontSize: '16px',
                    color: '#8E8877'
                  },
                },
                "gateway": {
                    "xname": "Binding",
                    "props": {
                        "binding": {
                            "subText": "content"
                        }
                    }
                }
            }
         ],
        },
        "cart": {
          "xname": "Cart",
          "props": {
            "padding": "12px 70px 12px 12px",
            "margin": "0",
            "linewidth": "0px",
            "corner": "8px",
            "fill": "#1E2128"
          }
        },
        selector: {
          xname: item.componentType,
          props: item.componentProps
        },
        "container": "SelectList",
      }

      const textLayoutConfig = {
        presenter: {
          xname: 'Text',
          props: {
          }
        },
        "cart": {
          "xname": "Cart",
          "props": {
            "padding": "6px 40px",
            "margin": "1px 0",
            "linewidth": "1px",
            "corner": "8px"
          }
        },
        selector: {
          xname: item.componentType,
          props: item.componentProps
        },
        "container": "SelectList",
      }
      setPreviewLayout({ textLayoutConfig, iconLayoutConfig, icon2LayoutConfig, cardLayoutConfig })
    }
  }


  return (
    <ChakraProvider>
      <VStack align='stretch' spacing='-2'>
        <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
          <Button colorScheme='teal' size='sm' marginRight={'8px'} onClick={() => goViewPage()}>
            返回
          </Button>
        </Box>

        <HStack spacing={'1'}>
          <Box style={{ height: '100vh', padding: '8px' }}>
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
    </ChakraProvider>
  )

}