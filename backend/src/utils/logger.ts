export function logDebug(message: string): void {
    console.debug("DEBUG: " + treat(message));
}

export function logInfo(message: string): void {
    console.info("INFO: " + treat(message));
}

export function logError(message: string): void {
    console.error("ERROR: " + treat(message));
}

function treat(message: string): string {
    return message.replace(/\s+/g, " ");
}
