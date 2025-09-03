import { AppUpdaterPlugin } from '@m430/capacitor-app-updater';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    AppUpdaterPlugin.echo({ value: inputValue })
}
