import React, { Component } from 'react';

import style from './index.less'

class Progress extends Component {
  state = {
    show: false,
  }
  start() { // 开始显示
    this.setState({
      show: true
    });
  }
  done() { // 结束隐藏
    this.setState({
      show: false
    });
  }

  render() {
    return (
      <div className={style.progress} style={this.state.show ? { display: 'block' } : { display: 'none' }}>
        <div className={style.bar}>
          <div className={style.peg}></div>
        </div>
        <div className={style.spinner}>
          <div className={style['spinner-icon']}></div>
        </div>
      </div>
    )
  }
}
export default Progress;