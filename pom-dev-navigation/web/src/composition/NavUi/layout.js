module.exports = {
  xname: 'Gridbox',
  props: {
    columns: 6 //列数
  },
  presenter: {
    xname: 'Flexbox',
    props: {
      direction: 'column',
      justify: 'center row'
    },
    children: [
      {
        presenter: 'DefaultAvatar',
        binding: {
          path: 'url'
        },
        indicator:{
          xname:'ClickIndicator',
          binding: {
            "id":"id",
            "path":"path",
            "name":"name",
          }
        },
      },
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
      padding: '5px 10px',
      margin: '5px',
      linewidth: 0
    }
  },
  indicator:{
    xname:'ManageMenuIndicator',
    props:{
      action: {
        deleteAPI: '/api/pub/data/services/navigation/(id)'
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
        createAPI: '/api/pub/data/services/navigation',
        getAPI: '/api/pub/data/services/navigation/(id)',
        updateAPI: '/api/pub/data/services/navigation/(id)',
        deleteAPI: '/api/pub/data/services/navigation/(id)'
      },
      fields: [{
        label: '名称',
        field: 'name',
        type: 'input',
        defaultValue: '',
        rules: {
          isRequired: true
        },
        props:{
          placeholder: '网站名称',
        }
      },
      {
        label: '图片',
        field: 'url',
        type: 'input',
        rules: {
          isRequired: true
        },
        props:{
          placeholder: '图片链接',
        }
      },
      {
        label: '链接',
        field: 'path',
        type: 'input',
        rules: {
          isRequired: true
        },
        props:{
          placeholder: '本地链接/第三方以http开头'
        }
      },
      {
        label: '类别',
        field: 'typeId',
        type: 'select-fetch',
        rules: {
          isRequired: true
        },
        props: {
          placeholder: '选择类别'
        },
        saveData:{ //额外提交的字段和值
          typeName: 'name'
        },
        options: {
          api: '/api/pub/data/services/navCategory',
          label: 'name',
          value: 'id',
        }
      },
      {
        label: '描述',
        field: 'desc',
        type: 'input',
        props: {
          placeholder: '描述属性'
        }
      },
      
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
      //     api: '/api/crud/api_model/apiTableModel/apiTableModels',
      //     label: 'name',
      //     value: 'id',
      //     itemField:'fieldModelId', // 数组的 item 承载字段
      //   }
      // },
      ]
    }
  }
};
