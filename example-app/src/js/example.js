import { AppInstallPlugin } from '@m430/capacitor-app-install';

window.testCanInstallUnknownApps = async () => {
    try {
        const result = await AppInstallPlugin.canInstallUnknownApps();
        console.log('Can install unknown apps:', result.granted);
        alert(`Can install unknown apps: ${result.granted}`);
    } catch (error) {
        console.error('Error:', error);
        alert(`Error: ${error.message}`);
    }
};

window.testInstallApk = async () => {
    const filePath = document.getElementById("filePathInput").value;
    if (!filePath) {
        alert('Please enter a file path');
        return;
    }
    try {
        await AppInstallPlugin.installApk({ filePath });
        alert('APK installation initiated');
    } catch (error) {
        console.error('Error:', error);
        alert(`Error: ${error.message}`);
    }
};

window.testFilePermission = async () => {
    try {
        const result = await AppInstallPlugin.hasFilePermission();
        console.log('Has file permission:', result.granted);
        alert(`Has file permission: ${result.granted}`);
    } catch (error) {
        console.error('Error:', error);
        alert(`Error: ${error.message}`);
    }
};
