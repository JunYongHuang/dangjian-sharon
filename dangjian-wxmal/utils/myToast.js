function getCtx (selector) {
    const pages = getCurrentPages();
    const ctx = pages[pages.length - 1];

    const componentCtx = ctx.selectComponent(selector);

    if (!componentCtx) {
        console.error('无法找到对应的组件，请按文档说明使用组件');
        return null;
    }
    return componentCtx;
}

function MyToast(options) {
    const { selector = '#myToast' } = options;
    const ctx = getCtx(selector);

    ctx.handleShow(options);
}

MyToast.hide = function (selector = '#myToast') {
    const ctx = getCtx(selector);

    ctx.handleHide();
};

function Message(options) {
    const { selector = '#message' } = options;
    const ctx = getCtx(selector);

    ctx.handleShow(options);
}

module.exports = {
    $MyToast: MyToast,
    $Message: Message
};