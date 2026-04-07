#!/bin/bash

JAVA_VERSION="25.0.2-tem"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[1;34m'
NC='\033[0m'

print_error() { echo -e "${RED}❌  $1${NC}"; }
print_success() { echo -e "${GREEN}✅  $1${NC}"; }
print_warning() { echo -e "${YELLOW}⚠️  $1${NC}"; }
print_info() { echo -e "${BLUE}ℹ️  $1${NC}"; }

echo ""
echo "🔍 Checking Java $JAVA_VERSION requirements..."
echo ""

check_sdkman() {
    print_info "Checking SDKMAN..."
    
    if [ -d "$HOME/.sdkman" ]; then
        print_success "SDKMAN is installed"
        export SDKMAN_DIR="$HOME/.sdkman"
        [[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"
        return 0
    else
        print_error "SDKMAN not found!"
        echo ""
        echo "🔧 Install SDKMAN: curl -s \"https://get.sdkman.io\" | bash"
        echo "🔄 Then run: source \"$HOME/.sdkman/bin/sdkman-init.sh\""
        return 1
    fi
}

setup_java() {
    print_info "Setting up Java $JAVA_VERSION..."
    
    if ! command -v sdk &> /dev/null; then
        print_error "'sdk' command not found"
        echo "🔄 Run: source \"$HOME/.sdkman/bin/sdkman-init.sh\""
        return 1
    fi
    
    sdk install java "$JAVA_VERSION" > /dev/null 2>&1
    
    if [ $? -ne 0 ]; then
        print_error "Failed to install Java $JAVA_VERSION"
        return 1
    fi
    
    sdk use java "$JAVA_VERSION" > /dev/null 2>&1
    
    echo ""
    if java -version 2>&1 | grep -q "version \"25"; then
        print_success "Java $(java -version 2>&1 | head -1) is active"
        return 0
    else
        print_error "Failed to activate Java $JAVA_VERSION"
        echo "📊 Current: $(java -version 2>&1 | head -1)"
        return 1
    fi
}

check_maven() {
    print_info "Checking Maven..."
    
    if command -v mvn &> /dev/null; then
        print_success "Maven found: $(mvn -version | head -1)"
        return 0
    else
        print_warning "Maven not found"
        
        if command -v sdk &> /dev/null; then
            echo "🔧 Install: sdk install maven"
        else
            echo "📦 Download: https://maven.apache.org/download.cgi"
        fi
        return 1
    fi
}

main() {
    check_sdkman || exit 1
    echo ""
    setup_java || exit 1
    echo ""
    check_maven
    echo ""
    print_success "🎯 Java $JAVA_VERSION environment is ready!"
    echo ""
}

main