"""
    Copyright 2013 Michael Adams, CJ Done, Charles Eswine, Kyle Falconer, 
    Will Gorman, Stephen Kaysen, Pat McCroskey and Matthew Swinney

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
"""

import sys
from cx_Freeze import setup, Executable       

# Dependencies are automatically detected, but it might need fine tuning.
build_exe_options = {"packages": ["os"], "includes": ["Tkinter", "sys", "traceback"], "silent":True, "optimize":2}

# GUI applications require a different base on Windows (the default is for a
# console application).
base = None
if sys.platform == "win32":
    base = "Win32GUI"

setup(  name = "ConfigSetup",
        version = "0.1",
        description = "Configuration Utility for the Court Scheduling application",
        options = {"build_exe": build_exe_options},
        executables = [Executable("configSetup.py", base=base)])