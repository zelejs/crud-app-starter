module.exports = {
  xname: 'Wrap',
  props: {
  },
  presenter: {
    xname: 'Flexbox',
    props: {
      direction: 'column',
      justify: 'center row'
    },
    children: [
      // {
      //   presenter: 'DefaultAvatar',
      //   binding: {
      //     url: 'url'
      //   },
      // },
      {
        presenter: "Text",
        binding: {
          name: "content"
        },
        props:{
          marginTop: '8px'
        }
      }
    ],
  },
  cart: {
    xname: 'Cart',
    props: {
      // fill:'#ccc',
      // fillHover:'#FFFBE5',
      padding: '12px 25px',
      margin: '5px',
      linewidth: '0'
    }
  },
  indicator:{
    xname:'AutolayoutManageMenuIndicator',
    props:{
      action: {
        deleteAPI: '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/(id)'
      }
    },
    binding: {
      "id":"id",
      "path":"url",
      "name":"content",
    }
  },
  container: {
    xname: 'ManageList',
    props:{
      addnew: ''
    }
  },
  navigation: {
    model: {
      api: {
        createAPI: '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules',
        getAPI: '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/(id)',
        updateAPI: '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/(id)',
        deleteAPI: '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/(id)'
      },
      fields: [{
        label: '显示名称',
        field: 'name',
        type: 'input',
        defaultValue: '',
        // rules: {
          // isRequired: true
        // },
        props:{
          placeholder: '显示名称',
        }
      },
      {
        label: '组件标识',
        field: 'moduleName',
        type: 'input',
        props: {
          placeholder: '组件标识'
        }
      },
      {
        label: '组件类型',
        field: 'componentOption',
        type: 'input',
        props: {
          defaultValue: 'autolayout',
          isReadOnly: true
        }
      },
      // {
      //   label: '组件名称',
      //   field: 'componentType',
      //   type: 'input',
      //   // rules: {
      //     // isRequired: true
      //   // },
      //   props:{
      //     placeholder: '组件名称'
      //   }
      // },
      // {
      //   label: '标准组件',
      //   field: 'componentId',
      //   type: 'select-fetch',
      //   // rules: {
      //     // isRequired: true
      //   // },
      //   props: {
      //     placeholder: '选择标准组件'
      //   },
      //   saveData:{ //额外提交的字段和值
      //     // typeName: 'name'
      //   },
      //   options: {
      //     api: '/openapi/crud/lc_low_auto_component/lowAutoComponent/lowAutoComponents?pageNum=1&pageSize=1000',
      //     label: 'componentName',
      //     value: 'id',
      //   }
      // },
      // {
      //   label: 'layout',
      //   field: 'descriptor',
      //   type: 'input',
      //   rules: {
      //     isRequired: false
      //   },
      //   props:{
      //     placeholder: '组件的layout.json',
      //   }
      // },
      
      // {
      //   label: '复选框',
      //   field: 'checkedIds',
      //   type: 'checkbox-fetch',
      //   rules: {
      //     isRequired: false
      //   },
      //   props: {
      //     placeholder: '请选择'
      //   },
      //   saveData:{ //额外提交的字段和值
      //     fieldName: 'name',
      //     fieldModelName: 'modelName'
      //   },
      //   options: {
      //     modalTitle:'选择字段',
      //     api: '/openapi/crud/lc_low_auto_component/lowAutoComponent/lowAutoComponents',
      //     label: 'componentName',
      //     value: 'id',
      //     itemField:'fieldModelId', // 数组的 item 承载字段
      //   }
      // },
      ]
    }
  }
};
