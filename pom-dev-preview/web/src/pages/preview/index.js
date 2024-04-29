import React from 'react';
import { ChakraProvider, VStack, HStack } from '@chakra-ui/react';
import DataFlowContainer from 'zero-element-boot/lib/components/container/DataFlowContainer';
import MultiActionsIndicator from 'zero-element-boot/lib/components/indicator/MultiActionsIndicator';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import { LS } from 'zero-element/lib/utils/storage';
import qs from 'qs';

export default function Preview ( props ) {

    const { layoutName } = props.location.query ||  qs.parse(props.location.search.split('?')[1])

    const obj = {
        layoutName: layoutName,
    }
    LS.set("commonData", obj)
    const _actions = [
        //new
        [
            {
                xname: 'PreviewSelectAction',
                props: {
                    selection: {
                        xname: 'NewPresentersAutolayout',
                        props: {
                        },
                        label: 'NewPresenter',
                    },
                    api: '/openapi/lc/module/presenter/based-on-presenter-create-presenter',
                    converter: {
                        "layoutName": "mainModuleName",
                        "id": "addModuleId"
                    },
                },
            },
            {
                xname: 'PreviewSelectAction',
                props: {
                    selection: {
                        xname: 'NewCartsAutolayout',
                        props: {
                        },
                        label: 'NewCart',
                    },
                    api: '/openapi/lc/module/presenter/based-on-presenter-create-presenter',
                    converter: {
                        "layoutName": "mainModuleName",
                        "id": "addModuleId"
                    },
                },
            },
            {
                xname: 'PreviewSelectAction',
                props: {
                    selection: {
                        xname: 'NewIndicatorsAutolayout',
                        props: {
                        },
                        label: 'NewIndicator',
                    },
                    api: '/openapi/lc/module/presenter/based-on-presenter-create-presenter',
                    converter: {
                        "layoutName": "mainModuleName",
                        "id": "addModuleId"
                    },
                },
            },
            {
                xname: 'PreviewSelectAction',
                props: {
                    selection: {
                        xname: 'NewContainersAutolayout',
                        props: {
                        },
                        label: 'NewContainer',
                    },
                    api: '/openapi/lc/module/presenter/based-on-presenter-create-presenter',
                    converter: {
                        "layoutName": "mainModuleName",
                        "id": "addModuleId"
                    },
                },
            },
            {
                xname: 'NewDatasetAction',
                props: {
                },
            }
        ],

        //change
        [
            {
                xname: 'PreviewSelectAction',
                props: {
                    selection: {
                        xname: 'NewPresentersAutolayout',
                        props: {
                        },
                        label: 'AddPresenter',
                    },
                    api: '/openapi/lc/module/add-child-module',
                    converter: {
                        "layoutName": "mainModuleName",
                        "id": "addModuleId"
                    },
                    method:'PATCH'
                },
            },
            {
                xname: 'PreviewSelectAction',
                props: {
                    selection: {
                        xname: 'NewCartsAutolayout',
                        props: {
                        },
                        label: 'ChangeCart',
                    },
                    api: '/openapi/lc/module/replace-add-child-module',
                    converter: {
                        "layoutName": "mainModuleName",
                        "id": "replaceModuleId"
                    },
                },
            },
            {
                xname: 'PreviewSelectAction',
                props: {
                    selection: {
                        xname: 'NewIndicatorsAutolayout',
                        props: {
                        },
                        label: 'ChangeIndicator',
                    },
                    api: '/openapi/lc/module/replace-add-child-module',
                    converter: {
                        "layoutName": "mainModuleName",
                        "id": "replaceModuleId"
                    },
                },
            },
            {
                xname: 'PreviewSelectAction',
                props: {
                    selection: {
                        xname: 'NewContainersAutolayout',
                        props: {
                        },
                        label: 'ChangeContainer',
                    },
                    api: '/openapi/lc/module/replace-add-child-module',
                    converter: {
                        "layoutName": "mainModuleName",
                        "id": "replaceModuleId"
                    },
                },
            },
            {
                xname: 'PreviewSelectAction',
                props: {
                    selection: {
                        xname: 'NewLayoutsAutolayout',
                        props: {
                        },
                        label: 'ChangeLayout',
                    },
                    api: '/openapi/lc/module/replace-add-child-module',
                    converter: {
                        "layoutName": "mainModuleName",
                        "id": "replaceModuleId"
                    },
                },
            },
            {
                xname: 'PreviewSelectAction',
                props: {
                    selection: {
                        xname: 'NewSelectorsAutolayout',
                        props: {
                        },
                        label: 'ChangeSelector',
                    },
                    api: '/openapi/lc/module/replace-add-child-module',
                    converter: {
                        "layoutName": "mainModuleName",
                        "id": "replaceModuleId"
                    },
                },
            },
            {
                xname: 'ChangeDatasetAction',
                props: {
                },
            }
        ],

        // no
        [
            
            {
                xname: 'NoPresenterAction',
                props: {
                    selection: {
                        xname: 'GetPresentersAutolayout',
                        props: {
                        },
                        label: 'NoPresenter',
                    },
                },
            },
            {
                xname: 'DeleteAction',
                props: {
                    api: '/openapi/lc/module/remove-child-module-of-presenter-option',
                    converter: {
                        "layoutName": "mainModuleName",
                    },
                    label: 'NoLastPresenter'
                },
            },
            {
                xname: 'DeleteAction',
                props: {
                    api: '/openapi/lc/module/presenter/remove-presenter-child-module',
                    converter: {
                        "layoutName": "mainModuleName",
                    },
                    apiBody: {
                        removeModuleOption: 'cart'
                    },
                    label: 'NoCart'
                },
            },
            {
                xname: 'DeleteAction',
                props: {
                    api: '/openapi/lc/module/presenter/remove-presenter-child-module',
                    converter: {
                        "layoutName": "mainModuleName",
                    },
                    apiBody: {
                        removeModuleOption: 'indicator'
                    },
                    label: 'NoIndicator'
                },
            },
            {
                xname: 'DeleteAction',
                props: {
                    api: '/openapi/lc/module/presenter/remove-presenter-child-module',
                    converter: {
                        "layoutName": "mainModuleName",
                    },
                    apiBody: {
                        removeModuleOption: 'container'
                    },
                    label: 'NoContainer'
                },
            },
            {
                xname: 'DeleteAction',
                props: {
                    api: '/openapi/lc/module/presenter/remove-presenter-child-module',
                    converter: {
                        "layoutName": "mainModuleName",
                    },
                    apiBody: {
                        removeModuleOption: 'layout'
                    },
                    label: 'NoLayout'
                },
            },
            {
                xname: 'DeleteAction',
                props: {
                    api: '/openapi/lc/module/presenter/remove-presenter-child-module',
                    converter: {
                        "layoutName": "mainModuleName",
                    },
                    apiBody: {
                        removeModuleOption: 'selector'
                    },
                    label: 'NoSelector'
                },
            },
        ],

        //other
        [
            //params
            {
                xname: 'NewParamAction',
                props: {
                    selection: {
                        xname: 'PropsManageAutolayout',
                        props: {
                        },
                        label: 'Params',
                    },
                },
            },
            //props
            {
                xname: 'PropsAction',
                props: {
                },
            },
            //binding
            {
                xname: 'BindingAction',
                props: {
                },
            },
            //sort presenter
            {
                xname: 'SortPresenterAction',
                props: {
                },
            },
            //dataset binding
            {
                xname: 'DatasetBindingAction',
                props: {
                },
            }
        ],

    ]

    const converter = {
    }

    return (
        <ChakraProvider>
            <DataFlowContainer converter={converter}>
                <VStack alignItems={'flex-start'} spacing={5}>
                    <MultiActionsIndicator actions={_actions} alignment='topleft' />
                    <PreviewAutoLayout />
                </VStack>
            </DataFlowContainer>
        </ChakraProvider>
    )
}