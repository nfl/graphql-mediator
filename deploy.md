# Prerequisites
- A GPG key to sign the jar
- Credentials for Bintray
- **Optional**: Credentials for Sonatype

# Build and deploy a new version to Bintray

```bash
export BINTRAY_USER=bintrayUser BINTRAY_KEY=bintrayeApiKey SONATYPE_USERNAME=sonatypeUser SONATYPE_PASSWORD=sonatypePassword
cd ~/projects/graphql-mediator
gradle install
gradle bintrayUpload
```

# Optionally, sync to Maven Central
On the version page go to the Maven Central tab (the one with the dinosaur icon on it),
enter your Sonatype password and click “Sync” and you’re done!
Your package is now in [Releases](https://oss.sonatype.org/content/repositories/releases)
and will be synced to Maven Central (and they usually take their time).

## Note
For this syncing to work, it must be enabled (1-time action)
by creating a ticket at https://issues.sonatype.org/ to turn on synchronization
of this project from Sonatype to Maven Central.

# Useful links
- An easy and concise guide: https://blog.bintray.com/2014/02/11/bintray-as-pain-free-gateway-to-maven-central/