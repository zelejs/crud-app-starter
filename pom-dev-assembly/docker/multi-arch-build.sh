## create docker driver for buildx
docker buildx create --name dev_assembly --config buildkitd.toml --use 

## start buildx
docker buildx build \
--push \
--platform linux/arm/v7,linux/arm64/v8,linux/amd64 \
--tag 192.168.3.45:5000/dev:assembly .

## use back default driver
docker buildx use default
docker buildx rm dev_assembly

