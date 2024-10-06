# Use the official Golang image as a parent image
FROM golang:1.21-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the local package files to the container's workspace
COPY . .

# Build the Go app
RUN go build -o main ./cmd/main.go

# Expose port 3000
EXPOSE 3000

# Run the binary program produced by `go build`
CMD ["/app/main"]