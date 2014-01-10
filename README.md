Shipp3r
=======

## Introduction ##

This utility can be used to move an entire directory from local disk to S3. Upload to S3 is done by the jets3t library. This allows the upload to be bandwith throttled.

## Basic Usage ##

Shipp3r must be built from source. Once you have the source and a recent version of sbt(>=0.13). You can run `sbt pack-archive` in the top level of the cloned source tree to generate a tarball with dependencies and run-scripts

```
Usage: shipp3r [options]

  --help
        
  -p <path to jets3t properties file> | --properties <path to jets3t properties file>
        Default value: jets3t.properties
  -l <local directory path> | --local-directory <local directory path>
        Default value: /home/fanatoly/workspace
  -r <S3 path to move file into> | --remote-directory <S3 path to move file into>
        
  -d | --dry-run
```

## Sample Properties File ##
```
aws.accesskey = <REDACTED>
aws.secretkey = <REDACTED>
httpclient.read-throttle=512
```


# References #

- [jets3t library](http://github.com/hyperic/jets3t)
