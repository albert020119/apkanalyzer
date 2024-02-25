import React, { useState } from 'react';
import axios from 'axios';
import './App.css';
import androidImage from './assets/android.png'; // Update the import statement

function App() {
  const [selectedFile, setSelectedFile] = useState(null);

  const handleDrop = (event) => {
    event.preventDefault();
    const droppedFile = event.dataTransfer.files[0];
    setSelectedFile(droppedFile);
  };

  const handleDragOver = (event) => {
    event.preventDefault();
  };

  const handleClick = async () => {
    if (!selectedFile) {
      console.error('No file selected');
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      const response = await axios.post('http://127.0.0.1:8000/apk', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      console.log('File(s) uploaded successfully!');
      console.log(response.data);
    } catch (error) {
      console.error('Failed to upload file(s):', error);
    }
  };

  const handleClear = () => {
    setSelectedFile(null);
  };

  const styles = {
    container: {
      border: '2px dashed #aaa',
      padding: '20px',
      textAlign: 'center',
      width: '300px', // Adjust the width as needed
      margin: '20px auto', // Center the box horizontally
      overflow: 'hidden', // Hide overflow content
    },
    fileList: {
      listStyle: 'none',
      padding: 0,
    },
    fileName: {
      whiteSpace: 'nowrap', // Prevent line breaks
      overflow: 'hidden', // Hide overflow content
      textOverflow: 'ellipsis', // Show ellipsis (...) for long text
    },
    clearButton: {
      marginTop: '10px',
    },
  };

  return (
    <div className="App">
      <header className="App-header">
        <img src={androidImage} className="App-logo" alt="androidImage" />
        <div
          onDrop={handleDrop}
          onDragOver={handleDragOver}
          style={styles.container}
        >
          <p>Drag and drop a file here</p>
            <div>
              <p>Uploaded Files:</p>
              <ul style={styles.fileList}>
                {selectedFile && (
                  <li style={styles.fileName}>
                    {selectedFile.name}
                  </li>
                )}
              </ul>
              <button onClick={handleClear} style={styles.clearButton}>
                Clear File List
              </button>
            </div>
          <button onClick={handleClick}>
            Send POST Request with File
          </button>
        </div>
      </header>
    </div>
  );
}

export default App;
