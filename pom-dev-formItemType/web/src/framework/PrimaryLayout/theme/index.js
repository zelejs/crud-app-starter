import window from 'zero-element/lib/utils/window';

function init() {
  const orignalSetItem = localStorage.setItem;

  localStorage.setItem = function (key, newValue) {
    if (key === 'theme') {
      const onThemeChange = new Event("onThemeChange");
      onThemeChange.newValue = newValue;
      window.dispatchEvent(onThemeChange);
    }
    // if (key === 'nav') {
    //   const onNavChange = new Event("onNavChange");
    //   onNavChange.newValue = newValue;
    //   window.dispatchEvent(onNavChange);
    // }
    orignalSetItem.apply(this, arguments);
  }

  window.addEventListener("onThemeChange", function (e) {
    changeTheme(e.newValue);
  });
}

function changeTheme(value) {
  window.less.modifyVars({
    '@primary-color': value,
  });
}

export {
  init,
  changeTheme,
}