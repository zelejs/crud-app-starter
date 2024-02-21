import React, { useState } from 'react';
import { VStack, HStack, Box } from '@chakra-ui/react';
import { history } from 'umi';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import LocalPreview from '@/composition/localPreview/view';

export default function Index(props) {

  const { id } = props.location && (props.location.query || qs.parse(props.location.search.split('?')[1]))
  const api = '/openapi/lc/module?componentOption=indicator'
  const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/161'

  const [previewLayout, setPreviewLayout] = useState('')


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
        indicator: {
          xname: item.componentType,
          props: item.componentProps
        }
      }

      const icon2LayoutConfig = {
        presenter: {
          xname: 'ImageSize',
          props: {
            width: 144,
            height: 'auto'
          }
        },
        "cart": {
          "xname": "CssCart",
          "props": {
            style:{
              "padding": "0px 6px",
              "margin": "1px 0",
              borderRadius: '8px',
              overflow: 'hidden',
            }
            
          }
        },
        indicator: {
          xname: item.componentType,
          props: item.componentProps
        }
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
        indicator: {
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
        indicator: {
          xname: item.componentType,
          props: item.componentProps
        }
      }
      setPreviewLayout({ textLayoutConfig, iconLayoutConfig, icon2LayoutConfig, cardLayoutConfig })
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