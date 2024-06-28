import React, { useEffect, useState } from 'react';
import { View, Text, Button, PermissionsAndroid, Platform, Alert } from 'react-native';
import { NativeModules } from 'react-native';

const { AppUsage } = NativeModules;

const App = () => {
  const [usageStats, setUsageStats] = useState<string>('');

  useEffect(() => {
    if (Platform.OS === 'android') {
      requestUsageStatsPermission();
    }
  }, []);

  const requestUsageStatsPermission = async () => {
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.PACKAGE_USAGE_STATS,
        {
          title: 'Usage Stats Permission',
          message: 'This app needs access to your usage stats to track app usage.',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        }
      );
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('You can use the usage stats');
        checkPermission();
      } else {
        console.log('Usage stats permission denied');
      }
    } catch (err) {
      console.warn(err);
    }
  };

  const checkPermission = async () => {
    try {
      await AppUsage.checkForPermission();
      console.log('Permission granted');
      getUsageStats();
    } catch (error) {
      console.log(error);
      Alert.alert('Permission Error', 'Permission not granted. Please enable usage stats permission in settings.');
    }
  };

  const getUsageStats = async () => {
    try {
      const stats = await AppUsage.getUsageStats();
      setUsageStats(stats);
    } catch (error) {
      console.log(error);
      Alert.alert('Error', 'Failed to get usage stats.');
    }
  };

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text>App Usage Stats</Text>
      <Button title="Check Permission" onPress={checkPermission} />
      <Button title="Get Usage Stats" onPress={getUsageStats} />
      <Text>{usageStats}</Text>
    </View>
  );
};

export default App;
