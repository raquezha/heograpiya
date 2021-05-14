#!/bin/bash

echo "running all unit tests..."
./gradlew testDebugUnitTest
echo "test complete!"

echo "running KtLintFormat..."
./gradlew ktLintFormat
echo "format complete!"

echo "running KtLintFormat..."
./gradlew detekt