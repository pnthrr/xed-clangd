#!/bin/bash
set -e

source "$LOCAL/bin/utils"

INSTALL_DIR="$HOME/.lsp/clangd"

install() {
  info 'Installing clangd language server...'
  apt update && apt install -y clangd
  mkdir -p "$INSTALL_DIR"
  touch "$INSTALL_DIR/installed"
  info 'clangd installed successfully.'
  exit 0
}

uninstall() {
  info 'Uninstalling clangd language server...'
  apt remove -y clangd
  rm -rf "$INSTALL_DIR"
  info 'clangd uninstalled successfully.'
  exit 0
}

update() {
  info 'Updating clangd language server...'
  apt update && apt install -y clangd
  info 'clangd updated successfully.'
  exit 0
}

case "$1" in
  --uninstall) uninstall;;
  --update) update;;
  *) install;;
esac
