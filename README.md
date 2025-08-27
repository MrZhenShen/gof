Flatten code into single file: 

- Windows (PowerShell):
``` powershell
Get-ChildItem -Recurse -Filter *.java | Get-Content | Out-File all_java.txt
```
- Linux / macOS (bash):
``` bash
find . -name "*.java" -type f -exec cat {} + > all_java.txt
```
