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