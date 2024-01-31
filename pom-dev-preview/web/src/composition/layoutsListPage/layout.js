module.exports = {
    xname: 'Gridbox',
    props: {
        columns: 8,
    },
    binding: {
      'moduleName': 'content',
      'componentType': '__layout.xname',
      'componentProps': '__layout.props'
    },
    cart: {
      xname: 'Cart',
      props: {
        padding: '30px 10px',
        margin: '1px 0',
        linewidth: '1px',
        corner: '8px',
        // fill: '#edf2f7'
        isHover: false
      },
  
      indicator:
      {
        xname: 'ShadowIndicator',
        props: {
        }
      }, //hover 时用, 第一次向子组件转递时,  更名为 hoverIndicator
      selector: {
        xname: 'SelectedCartUpperRightIcon',
        props: {
          state: 'selected',
          padding: '0'
        }
      }
    },
    container: 'SelectList',
    presenter: {
      // xname: 'Flexbox',
      // props: {
      //     align: 'start',
      //     direction: 'row'
      // },
      presenter: {
        xname: 'Rectangle',
        props:{
          width: '40px',
          height: '25px',
          fill: '',
          margin: '0 8px 8px 0',
          corner: '8px',
          border: '1px solid #d1d1d1',
        }
      },
      container: 'PlainList',
    },
  };
  