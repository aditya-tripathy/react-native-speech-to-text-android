# react-native-speech-to-text

## Getting started

`$ npm install react-native-speech-to-text --save`

### Mostly automatic installation

`$ react-native link react-native-speech-to-text`

## Usage

```javascript
import SpeechToText from 'react-native-speech-to-text';

// TODO: What to do with the module?
const speechToTextHandler = aync () => {

    let data = await SpeechToText.startSpeech('Try saying something', 'en_IN');
    console.log('data: ', data);
}
```
