zygotebench
===========

zygotebench - a simple benchmark for process creation speed on Android


Benchmarking process creation speed can be useful if you develop any 'tool,extension,...' that has impact on operating system behavior. Zygotebench was originally developed for [PatchDroid](http://PatchDroid.com)

The benchmark works by creating a new process through starting a service (the service is started as a separate process). The benchmark consists of 10 iterations. After each iteration the service process is killed to be restarted afterward.

A precompiled APK can be downloaded here: [zygotebench.apk](https://github.com/crmulliner/zygotebench/blob/master/zygotebench.apk)
