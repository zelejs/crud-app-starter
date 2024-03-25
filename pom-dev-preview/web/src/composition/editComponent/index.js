import React, { useState, useEffect } from 'react';
import { Box, HStack, InputGroup, Input, InputRightElement, Button, Text, useToast } from '@chakra-ui/react';
import { AutoLayout } from 'zero-element-boot';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');
import menuLayoutJson from './menuLayout';
import { propsManageLayout, propsManageConverter,  bindingManageLayout, bindingManageConverter} from './manageConfig'

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
  binding: 0,
}

const managePageConfigMap = {
  // 属性管理
  props:{
    layout:propsManageLayout,
    converter:propsManageConverter,
  },
  // 绑定管理
  binding:{
    layout:bindingManageLayout,
    converter:bindingManageConverter,
  }
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
  const [matchLayoutName, setMatchLayoutName] = useState('')
  const [childMenuId, setChildMenuId] = useState(0)
  const [currentModuleName, setCurrentModuleName] = useState(moduleName)
  const [containerHeight, setContainerHeight] = useState(window.innerHeight - 72)
  const toast = useToast()

  useEffect(_ => {
    setMenuName()
  }, [])

  useEffect(_ => {
    if (menuName) {
      // setComponentData('card');
      // 获取子组件
      getComponentDetail(menuName)
      const newApi = originApi + '&componentOption=' + menuName
      const newLayoutApi = originLayoutApi + '/' + componentListIdmap[menuName]
      setApi(newApi)
      setLayoutApi(newLayoutApi)
      setMatchLayoutName(menuName)
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
    if (!childMenuId) {
      toastTips('请选择要替换的组件')
      return
    }
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
        getComponentDetail(menuName)
        toastTips('修改成功')
      } else {
        toastTips('修改失败')
      }
    }).finally(_ => {
    });

  }

  const menuItemClick = (item) => {
    setApi('')
    setLayoutApi('')
    setChildApi('')
    setChildLayoutApi('')
    setMatchLayoutName('')
    setMenuName(item.name)

  }

  const childMenuClick = (item) => {
    setChildMenuId(item.id)
  }

  //选择组件
  const onSelectComponentItemClick = (item) => {
    console.log('onSelectComponentItemClick = ', item)
    editComponent(item.id)
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
    <HStack spacing={'0'}>
      <Box style={{
        width: '185px', height: '100vh', padding: '10px 20px', background: '#fff', display: 'flex',
        alignItems: 'flex-start', justifyContent: 'center', flexDirection: 'column'
      }}>
        <Text fontSize={'14px'} fontWeight={'bold'} marginBottom={1}>修改名称</Text>
        <Input placeholder='名称' defaultValue={currentModuleName} onChange={handleChangeInputValue} />
        <Button h='1.75rem' size='sm' margin={'8px 8px 20px 8px'} onClick={() => editModuleName()}>
          确定
        </Button>

        <Text fontSize={'14px'} fontWeight={'bold'} marginBottom={1}>修改组件</Text>

        <AutoLayout layout={menuLayoutJson} isScroll={false} onItemClick={menuItemClick} />
      </Box>

      <Box style={{ width: '6px', height: '100vh' }} background={'#EDECF1'}></Box>
      {
        matchLayoutName == 'attribute' || matchLayoutName == 'props' || matchLayoutName == 'binding' ? (
          matchLayoutName == 'attribute' ? (
            <Box minW={'220px'} style={{ height: '100vh', padding: '10px 20px', background: '#fff' }}>
              <PreviewAutoLayout layoutName={'PropsManage'} moduleId={componentId} />
            </Box>
          ): matchLayoutName == 'props' || matchLayoutName == 'binding'  ? ( 
            
            <Box minW={'220px'} style={{ height: '100vh', padding: '10px 20px', background: '#fff' }}>
              <AutoLayout moduleId={componentId} {...managePageConfigMap[matchLayoutName]}/>
            </Box>
            
          ):<></>
        ) : (
          menuName ? (
            <Box display={'flex'} flex={1} flexDirection={'row'}>
              {childApi && childLayoutApi && (
                <Box className='child-module-container' style={{ height: '100vh', padding: '10px 20px', background: '#fff' }}>
                  
                  <PreviewAutoLayout layoutApi={childLayoutApi} containerHeight={containerHeight} api={childApi} onItemClick={childMenuClick} />
                </Box>
              )}
              <Box style={{ width: '6px', height: '100vh' }} background={'#EDECF1'}></Box>

              <Box flex={1} style={{ height: '100vh', padding: '10px 20px', background: '#fff' }}>
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