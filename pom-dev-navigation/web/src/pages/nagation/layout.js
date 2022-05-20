module.exports = {
  xname: 'Gridbox',
  props: {
    direction: 'column',
    justify: 'center row'
  },
  presenter: {
    xname: 'Flexbox',
    props: {
      direction: 'column',
      justify: 'center row'
    },
    children: [
      {
        presenter: 'Avatar',
        gateway: {
          xname: 'Binding',
          props: {
            binding: {
              name: 'url'
            }
          }
        }
      },
      {
        presenter: "Title",
        gateway: {
          xname: "Binding",
          props: {
            binding: {
              name: "titleText"
            }
          }
        }
      }]
  },
  cart: {
    xname: 'Cart',
    props: {
      padding: '5px 10px',
      margin: '5px',
      linewidth: 0
    }
  },
  container: 'SimCRUDList',
  navigation: {
    model: {
      delComfirmTips: true,
      api: {
        createAPI: '/api/pub/data/services/navigation',
        getAPI: '/api/pub/data/services/navigation/(id)',
        updateAPI: '/api/pub/data/services/navigation/(id)',
        deleteAPI: '/api/pub/data/services/navigation/(id)'
      },
      fields: [{
        label: '标题',
        field: 'name',
        type: 'input',
        rules: {
          isRequired: true
        },
        props: {
          placeholder: '请输入标题',
        }
      },
      {
        label: '图片',
        field: 'url',
        type: 'input',
        rules: {
          isRequired: true
        },
        props: {
          placeholder: '请输入图片',
        }
      },
      {
        label: '链接',
        field: 'path',
        type: 'input',
        rules: {
          isRequired: true
        },
        props: {
          placeholder: '请输入本地链接/第三方以http开头'
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
          placeholder: '请类别'
        },
        saveData: { //额外提交的字段和值
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
      ]
    }
  }
};
