import React from 'react';
import { ChakraProvider } from '@chakra-ui/react'
import { AddNewContainer } from 'zero-element-boot/lib/components/container';
import { APIContainer, AutoLayout } from 'zero-element-boot';
import ColorModal from 'zero-element-boot/lib/components/modalComponent/colorModal';
import PaletteList from '@/composition/paletteList';

export default function PaletteManage(props) {
    
    const api = "/openapi/lc/palette/palette-name-list"
    const deleteApi = "/openapi/lc/palette?paletteName=(paletteName)"

    const config = {
        addnewApi: '/openapi/lc/palette',
        saveApi: '/openapi/lc/palette/(id)',
    }

    const layoutJson = {
        "xname": "Gridbox",
        "props": {
            "columns": "5"
        },
        "container": {
            "xname": "SelectList",
            "props": {
            }
        },
        "cart": {
            "xname": "Cart",
            "props": {
                "padding": "5px",
                "margin": "0",
                "linewidth": 0,
                "corner": "0px"
            }
        },
        "indicator": {
            "xname": 'TitleIndicator',
            "props": {
            },
            "binding": {
                "paletteName": "titleContent",
            },
            "trigger": "always"
        },
        "presenter": {
            "xname": "Text",
            "binding": {
                "paletteName1": "content"
            },
            "indicator":{
                "xname": "CircularDeleteIndicator",
                "props": {
                    "isDisabled": true,
                },
                "binding": { "paletteName": "paletteName" },
            },
            "cart": {
                "xname": "SquareCart",
                "props": {
                    "margin": "0px",
                    "corner": "8px",
                    "fill": "",
                    "ratio": 0.5,
                }
            },
                
        }

    }

    return (
        <ChakraProvider>
            <AddNewContainer {...config}>
                <APIContainer API={api}>
                    <AutoLayout layout={layoutJson} action={deleteApi} />
                </APIContainer>
                <ColorModal>
                    <PaletteList/>
                </ColorModal>
            </AddNewContainer>
        </ChakraProvider>

    )


}
