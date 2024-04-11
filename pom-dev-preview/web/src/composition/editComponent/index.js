import React, { useState, useEffect, useRef } from 'react';
import { Box, HStack, InputGroup, Input, InputRightElement, Button, Text, useToast } from '@chakra-ui/react';
import { AutoLayout } from 'zero-element-boot';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');
import menuLayoutJson from './menuLayout';
import { propsManageLayout, propsManageConverter, bindingManageLayout, bindingManageConverter } from './manageConfig'
import DataSetManage from '@/composition/dataSetManage';

require('./index.less');

const componentListIdmap = {
  presenter: 160,
  cart: 129,
  indicator: 161,
  selector: 163,
  container: 270,
  layout: 153,
  gateway: 287,
  binding: 0,
}

const childComponentListMap = {
  presenter: 272,
  cart: 273,
  gateway: 286,
  container: 333,
  indicator: 334,
  selector: 335,
  layout: 336,
  binding: 0,
}

const managePageConfigMap = {
  // 属性管理
  props: {
    layout: propsManageLayout,
    converter: propsManageConverter,
  },
  // 绑定管理
  binding: {
    layout: bindingManageLayout,
    converter: bindingManageConverter,
  }
}

const otherMenuList = [
  'attribute', 'props', 'binding', 'dataSet', 'new dataset'
]

const addBtnMenuList = [
  'new presenter', 'new cart', 'new indicator', 'new container'
]

const componentNameMap ={
  'new presenter': 'presenter',
  'new cart': 'cart',
  'new indicator': 'indicator',
  'new container': 'container'
}


export default function EditComponent(props) {

  const { componentId, moduleName } = props;

  const originApi = '/openapi/lc/module?pageNum=1&pageSize=100'
  const originLayoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/'

  const [api, setApi] = useState()
  const [layoutApi, setLayoutApi] = useState()

  const [childApi, setChildApi] = useState()
  const [childLayoutApi, setChildLayoutApi] = useState()

  const [menuName, setMenuName] = useState('')
  const [childMenuId, setChildMenuId] = useState('')
  const [currentModuleName, setCurrentModuleName] = useState(moduleName)
  const [containerHeight, setContainerHeight] = useState(window.innerHeight - 72)
  const toast = useToast()
  const menuLayoutParentRef = useRef(null);
  const [menuLayoutHeight, setMenuLayoutHeight] = useState('')

  useEffect(_ => {
    if (menuLayoutParentRef.current) {
      // 先将容器高度设置为100%
      menuLayoutParentRef.current.style.height = '100%';
      // 获取容器的实际高度
      const height = menuLayoutParentRef.current.getBoundingClientRect().height;
      // 更新容器高度状态
      setMenuLayoutHeight(parseInt(height));
    }
  }, [])

  useEffect(_ => {
    if (menuName) {
      // setComponentData('card');
      let newMenuName = ''
      if(!addBtnMenuList.includes(menuName)){
        // 获取子组件
        newMenuName = menuName
        getComponentDetail(newMenuName)
      }else{
        newMenuName = componentNameMap[menuName]
      }
      const newApi = originApi + '&componentOption=' + newMenuName
      const newLayoutApi = originLayoutApi + '/' + componentListIdmap[newMenuName]
      setApi(newApi)
      setLayoutApi(newLayoutApi)
    }
  }, [menuName])

  //根据ID获取组件信息
  const getComponentDetail = (componentName) => {
    const api = `/openapi/lc/module/childModuleList/${componentId}?componentOption=${componentName}`
    const layout = `${originLayoutApi}${childComponentListMap[componentName]}`
    setChildApi(api)
    setChildLayoutApi(layout)

  }

  //修改moduleName
  const editModuleName = () => {
    if (!currentModuleName) {
      toastTips('请输入名称')
      return
    }
    const api = '/openapi/lc/module/' + componentId
    const query = {
      moduleName: currentModuleName
    }
    return promiseAjax(api, query, { method: 'PUT' }).then(resp => {
      if (resp && resp.code === 200) {
        toastTips('修改成功')
      } else {
        toastTips('修改失败')
      }
    }).finally(_ => {
    });

  }

  //修改组件
  const editComponent = (newComponentId) => {
    // if (!childMenuId) {
    //   toastTips('请选择要替换的组件')
    //   return
    // }
    const api = '/openapi/lc/module/replace-add-child-module'
    const query = {
      "mainModuleId": componentId,
      "removeModuleId": childMenuId,
      "replaceModuleId": newComponentId
    }

    return promiseAjax(api, query, { method: 'PATCH' }).then(resp => {
      if (resp && resp.code === 200) {
        setChildApi('')
        setChildLayoutApi('')
        setChildMenuId('')
        getComponentDetail(menuName)
        toastTips('修改成功')
      } else {
        toastTips('修改失败')
      }
    }).finally(_ => {
    });

  }


  //新增组件
  const addNewComponent = (item) => {
    const api = '/openapi/lc/module/presenter/based-on-presenter-create-presenter'
    const query = {
      "mainModuleId" : componentId,
      "addModuleId": item.id,
    }
    return promiseAjax(api, query, { method: 'POST' }).then(resp => {
      if (resp && resp.code === 200) {
        toastTips('新增成功')
      } else {
        toastTips('新增失败')
      }
    }).finally(_ => {
    });

  }

  const menuItemClick = (item) => {
    setApi('')
    setLayoutApi('')
    setChildApi('')
    setChildLayoutApi('')
    setMenuName(item.name)

  }

  const childMenuClick = (item) => {
    setChildMenuId(item.id)
  }

  const childMenuItemDelete = (status) => {
    if(status){
      setChildApi('')
      setChildLayoutApi('')
      getComponentDetail(menuName)
    }
  }

  //选择组件
  const onSelectComponentItemClick = (item) => {
    // console.log('onSelectComponentItemClick = ', item)
    editComponent(item.id)
  }

  //new component
  const onAddNewComponentClick = (item) => {
    if(item.isSelected){
      addNewComponent(item)
    }
  }

  const handleChangeInputValue = (e) => {
    setCurrentModuleName(e.target.value)
  }

  // tips
  function toastTips(text, status = 'success') {
    toast({
      title: text,
      description: "",
      status: status,
      duration: 3000,
      isClosable: true,
      position: 'top'
    })
  }

  return (
    <HStack spacing={'0'} alignItems={'flex-start'}>
      <Box style={{
        width: '185px', height: `${containerHeight + 10}px`, padding: '10px 20px', background: '#fff', display: 'flex',
        alignItems: 'flex-start', justifyContent: 'center', flexDirection: 'column',
        flexShrink: 0,
      }}>
        <Text fontSize={'14px'} fontWeight={'bold'} marginBottom={1}>修改名称</Text>
        <Input padding={'5px 10px'} placeholder='名称' defaultValue={currentModuleName} onChange={handleChangeInputValue} />
        <Button h='48px' padding={'5px 15px'} size='sm' margin={'8px 8px 10px 8px'} onClick={() => editModuleName()}>
          确定
        </Button>

        <Text fontSize={'14px'} fontWeight={'bold'} marginBottom={1}>修改组件</Text>

        <div ref={menuLayoutParentRef}>
          <AutoLayout layout={menuLayoutJson} isScroll={false} 
          onItemClick={menuItemClick}
          containerHeight={menuLayoutHeight} />
        </div>
      </Box>

      <Box style={{ width: '6px', height: `${containerHeight}px` }} background={'#EDECF1'}></Box>
      {
        otherMenuList.includes(menuName) ? (
          <Box minW={'220px'} style={{ height: `${containerHeight}px`, padding: '10px 20px', background: '#fff', overflow: 'hidden' }}>
            {
              menuName == 'attribute' ? (
                <PreviewAutoLayout layoutName={'PropsManage'} moduleId={componentId} />
              ) : menuName == 'props' || menuName == 'binding' ? (
                <AutoLayout moduleId={componentId} {...managePageConfigMap[menuName]} />
              ) : menuName === 'dataSet' || menuName === 'new dataset' ? (
                <DataSetManage moduleId={componentId} containerHeight={containerHeight} type={menuName} />
              ) : <></>
            }
          </Box>
        ) : addBtnMenuList.includes(menuName) ? (
          <Box w={'100%'} style={{ height: `${containerHeight}px`, padding: '10px 20px', background: '#fff' }}>
            {api && layoutApi && (
                  <PreviewAutoLayout layoutApi={layoutApi} isSwitch={false} containerHeight={containerHeight} api={api} onItemClick={onAddNewComponentClick} />
                )}
          </Box>
        ) : (
          menuName ? (
            <Box display={'flex'} flex={1} flexDirection={'row'}>
              {childApi && childLayoutApi && (
                <Box className='child-module-container' style={{ height: `${containerHeight}px`, padding: '10px 20px', background: '#fff' }}>
                  <PreviewAutoLayout layoutApi={childLayoutApi} containerHeight={containerHeight} moduleId={componentId} api={childApi}
                   onItemClick={childMenuClick} onItemDeleted={childMenuItemDelete} />
                </Box>
              )}
              <Box style={{ width: '6px', height: `${containerHeight}px` }} background={'#EDECF1'}></Box>

              <Box flex={1} style={{ height: `${containerHeight}px`, padding: '10px 20px', background: '#fff' }}>
                {api && layoutApi && (
                  <PreviewAutoLayout layoutApi={layoutApi} isSwitch={false} containerHeight={containerHeight} api={api} onItemClick={onSelectComponentItemClick} />
                )}
              </Box>
            </Box>
          ) : <></>

        )
      }

    </HStack>
  )
}