./mvnw clean package

cd target && mkdir demo

tar -xvzf phaser-demo-0.0.1-SNAPSHOT.jar -C demo

echo "Copying apt.yml with C shared dependencies to root of exploded Java application"
cp ../apt.yml ../maybe-next-time.wav demo/

echo "Ensure you have the apt-buildpack using a command like 'cf buildpacks'"
echo "Deploy your application: 'cf push my-app -b apt-buildpack -b java_buildpack_offline -s cflinuxfs3 -p target/demo'"
