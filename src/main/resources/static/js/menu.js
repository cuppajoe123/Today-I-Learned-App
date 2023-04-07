const menuButton = document.querySelector(".menu-icon");
let isVisible = false;
menuButton.addEventListener('pointerdown', () => {
    if (!isVisible) {
        document.querySelector(".links").style.display = 'block';
        isVisible = true;
    } else {
        document.querySelector(".links").style.display = 'none';
        isVisible = false;
    }
});

