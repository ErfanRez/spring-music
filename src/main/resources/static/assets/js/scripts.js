//Card Pagination

// Scroll left
function scrollContainerLeft(containerId) {
    const scrollableContainer = document.getElementById(containerId);
    const arrowLeft = scrollableContainer.previousElementSibling; // Left arrow
    scrollableContainer.scrollBy({ left: -600, behavior: 'smooth' });
    checkScrollPosition(scrollableContainer, arrowLeft, scrollableContainer.nextElementSibling);
}

// Scroll right
function scrollContainerRight(containerId) {
    const scrollableContainer = document.getElementById(containerId);
    const arrowRight = scrollableContainer.nextElementSibling; // Right arrow
    scrollableContainer.scrollBy({ left: 600, behavior: 'smooth' });
    checkScrollPosition(scrollableContainer, scrollableContainer.previousElementSibling, arrowRight);
}

// Check scroll position and disable/enable arrows
function checkScrollPosition(container, arrowLeft, arrowRight) {
    if (container.scrollLeft === 0) {
        arrowLeft.classList.add('disabled'); // Disable left arrow
    } else {
        arrowLeft.classList.remove('disabled'); // Enable left arrow
    }

    if (container.scrollLeft + container.clientWidth >= container.scrollWidth) {
        arrowRight.classList.add('disabled'); // Disable right arrow
    } else {
        arrowRight.classList.remove('disabled'); // Enable right arrow
    }
}

// Initial check for all containers
document.querySelectorAll('.scrollable-container').forEach(container => {
    const arrowLeft = container.previousElementSibling;
    const arrowRight = container.nextElementSibling;
    checkScrollPosition(container, arrowLeft, arrowRight);
    container.addEventListener('scroll', () => checkScrollPosition(container, arrowLeft, arrowRight));
});

/////////////////////////////////////////////////////////////////////////////////////////////

//Bootstrap error Toast

// Function to show the single error toast if there are validation errors
// function showErrorToast() {
//     const errorToast = document.getElementById('errorToast');
//     if (errorToast && errorToast.dataset.hasErrors === 'true') {
//         const toast = new bootstrap.Toast(errorToast);
//         toast.show();
//     }
// }

// // Call the function when the DOM is fully loaded
// document.addEventListener('DOMContentLoaded', showErrorToast);



// // Function to show all multiple error toasts
// function showErrorToasts() {
//     const toasts = document.querySelectorAll('.toast');
//     toasts.forEach(toast => {
//         const bootstrapToast = new bootstrap.Toast(toast);
//         bootstrapToast.show();
//     });
// }
//
// // Call the function when the DOM is fully loaded
// document.addEventListener('DOMContentLoaded', showErrorToasts);


/////////////////////////////////////////////////////////////////////////////////////////////

// Add album songs scripts

document.addEventListener('DOMContentLoaded', function () {
    const songInputsContainer = document.getElementById('songInputs');
    const addSongButton = document.getElementById('addSongButton');

    let songCount = 2; // Start counting from 3

    !!addSongButton && addSongButton.addEventListener('click', function () {
        songCount++;

        // Create new song title input
        const songTitleInput = document.createElement('div');
        songTitleInput.className = 'mb-3 song-input';
        songTitleInput.innerHTML = `
                <label for="songTitle${songCount}" class="form-label">Song Title</label>
                <input type="text" class="form-control" id="songTitle${songCount}" name="songTitles" required>
            `;

        // Create new song file input
        const songFileInput = document.createElement('div');
        songFileInput.className = 'mb-3 song-input';
        songFileInput.innerHTML = `
                <label for="songFile${songCount}" class="form-label">Song File</label>
                <input type="file" class="form-control" id="songFile${songCount}" name="songFiles" accept="audio/*" required>
                <div class="form-text">Only audio files (e.g., MP3, WAV) are allowed.</div>
            `;

        // Append new inputs to the container
        songInputsContainer.appendChild(songTitleInput);
        songInputsContainer.appendChild(songFileInput);
    });
});

/////////////////////////////////////////////////////////////////////////////////////////////

//disable submission button

function disableSubmitButton() {
    const submitButton = document.getElementById('submitButton');
    const addSongButton = document.getElementById("addSongButton");
    const loadingButton = document.getElementById("loadingButton");
    const loginButton = document.getElementById("loginButton");
    const registeringButton = document.getElementById("registeringButton");

    if (submitButton) {
        submitButton.disabled = true;
        submitButton.innerText = 'Saving...';
    } else {
        console.error("Submit button not found!");
    }

    if(addSongButton){
        addSongButton.disabled = true;
    }

    if (loadingButton) {
        loadingButton.disabled = true;
        loadingButton.innerText = 'Loading...';
    }

    if (loginButton) {
        loginButton.disabled = true;
        loginButton.innerText = 'Loading...';
    }

    if (registeringButton) {
        registeringButton.disabled = true;
        registeringButton.innerText = 'Loading...';
    }

}

/////////////////////////////////////////////////////////////////////////////////////////////

// Form submission success toast
function showToast() {
    const toastMessage = document.getElementById('toastMessage');
    if (toastMessage) {
        const toast = new bootstrap.Toast(toastMessage);
        toast.show();
    }
}

document.addEventListener('DOMContentLoaded', showToast);

/////////////////////////////////////////////////////////////////////////////////////////////

// Show modal when login button is clicked
document.getElementById('loginButton').addEventListener('click', function () {
    const loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
    loginModal.show();
});

/////////////////////////////////////////////////////////////////////////////////////////////

//Change img src in artist registration form

document.addEventListener('DOMContentLoaded', function () {
    const profilePictureInput = document.getElementById('profilePicture');
    const profilePicturePreview = document.getElementById('profilePicturePreview');

    // Open file dialog when the image is clicked
    profilePicturePreview.addEventListener('click', function () {
        profilePictureInput.click();
    });

    // Update the image preview when a file is selected
    profilePictureInput.addEventListener('change', function (event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                profilePicturePreview.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    });
});

/////////////////////////////////////////////////////////////////////////////////////////////

document.addEventListener("DOMContentLoaded", () => {
    // Elements
    const audio = document.getElementById("audio");
    const playBtn = document.getElementById("player-play");
    const playIcon = playBtn.querySelector("i");
    const progressContainer = document.getElementById("progress-container");
    const progressFilled = document.getElementById("progress-filled");
    const currentTimeEl = document.getElementById("player-current");
    const durationEl = document.getElementById("player-duration");
    const titleEl = document.getElementById("player-title");
    const artistEl = document.getElementById("player-artist");
    const coverEl = document.getElementById("player-cover");

    // State
    let isSeeking = false;
    let currentSong = null;

    // Initialize
    initPlayer();

    function initPlayer() {
        console.log("[Player] Initializing...");

        // Load only basic saved state (current song and play status)
        loadBasicState();

        // Setup event listeners
        setupEventListeners();
    }

    function loadBasicState() {
        try {
            const savedState = sessionStorage.getItem("playerState");
            if (savedState) {
                const state = JSON.parse(savedState);
                if (state.audioUrl) {
                    console.log("[Player] Loading basic saved state...");
                    currentSong = {
                        audioUrl: state.audioUrl,
                        title: state.title,
                        artistNickname: state.artistNickname,
                        coverUrl: state.coverUrl
                    };

                    // Update UI without resuming playback position
                    titleEl.textContent = currentSong.title || "Unknown Title";
                    artistEl.textContent = currentSong.artistNickname || "Unknown Artist";
                    coverEl.src = currentSong.coverUrl || "/assets/images/album-cover.webp";

                    // Load audio but don't set time
                    audio.src = currentSong.audioUrl;

                    // Update play/pause state
                    if (state.isPlaying) {
                        audio.play().catch(e => {
                            console.log("[Player] Autoplay prevented:", e);
                        });
                    }
                }
            }
        } catch (e) {
            console.log("[Player] Error loading state:", e);
            clearSavedState();
        }
    }

    function clearSavedState() {
        try {
            sessionStorage.removeItem("playerState");
        } catch (e) {
            console.log("[Player] Error clearing state:", e);
        }
    }

    function setupEventListeners() {
        // Play/Pause button
        playBtn.addEventListener("click", togglePlayback);

        // Progress bar seeking
        progressContainer.addEventListener("click", handleSeek);

        // Audio element events
        audio.addEventListener("timeupdate", updateProgress);
        audio.addEventListener("loadedmetadata", updateDuration);
        audio.addEventListener("ended", handlePlaybackEnd);
        audio.addEventListener("error", handleAudioError);
        audio.addEventListener("waiting", () => console.log("[Player] Buffering..."));
        audio.addEventListener("playing", () => console.log("[Player] Playback started"));
    }

    function formatTime(seconds) {
        if (isNaN(seconds)) return "0:00";
        const mins = Math.floor(seconds / 60);
        const secs = Math.floor(seconds % 60);
        return `${mins}:${secs < 10 ? "0" : ""}${secs}`;
    }

    function loadSong(song) {
        console.log(`[Player] Loading new song: ${song.title}`);

        try {
            // Store current song
            currentSong = song;

            // Update UI
            titleEl.textContent = song.title || "Unknown Title";
            artistEl.textContent = song.artistNickname || "Unknown Artist";
            coverEl.src = song.coverUrl || "/assets/images/album-cover.webp";

            // Reset audio element
            audio.pause();
            audio.src = "";
            audio.load();

            // Set new source
            audio.src = song.audioUrl;

            // Start playback
            audio.play()
                .then(() => {
                    playIcon.classList.replace("fa-play", "fa-pause");
                    saveBasicState();
                })
                .catch(e => {
                    console.log("[Player] Playback failed:", e);
                    playIcon.classList.replace("fa-pause", "fa-play");
                });

        } catch (e) {
            console.log("[Player] Error loading song:", e);
        }
    }

    function togglePlayback() {
        if (audio.paused) {
            audio.play()
                .then(() => {
                    playIcon.classList.replace("fa-play", "fa-pause");
                    saveBasicState();
                })
                .catch(e => {
                    console.log("[Player] Playback failed:", e);
                });
        } else {
            audio.pause();
            playIcon.classList.replace("fa-pause", "fa-play");
            saveBasicState();
        }
    }

    function updateProgress() {
        if (!isSeeking && audio.duration) {
            const percent = (audio.currentTime / audio.duration) * 100;
            progressFilled.style.width = `${percent}%`;
            currentTimeEl.textContent = formatTime(audio.currentTime);
        }
    }

    function updateDuration() {
        durationEl.textContent = formatTime(audio.duration);
    }

    function handleSeek(e) {
        if (!audio.duration) return;

        isSeeking = true;
        const width = progressContainer.clientWidth;
        const clickX = e.offsetX;
        audio.currentTime = (clickX / width) * audio.duration;

        setTimeout(() => {
            isSeeking = false;
        }, 100);
    }

    function handlePlaybackEnd() {
        console.log("[Player] Playback ended");
        playIcon.classList.replace("fa-pause", "fa-play");
        progressFilled.style.width = "0%";
        currentTimeEl.textContent = "0:00";
        saveBasicState();
    }

    function handleAudioError() {
        console.log(`[Player] Audio error: ${audio.error ? audio.error.message : 'Unknown error'}`);
        playIcon.classList.replace("fa-pause", "fa-play");
    }

    function saveBasicState() {
        try {
            const state = {
                audioUrl: audio.src,
                title: titleEl.textContent,
                artistNickname: artistEl.textContent,
                coverUrl: coverEl.src,
                isPlaying: !audio.paused
                // Note: We're intentionally not saving currentTime
            };
            sessionStorage.setItem("playerState", JSON.stringify(state));
            console.log("[Player] Basic state saved");
        } catch (e) {
            console.log("[Player] Error saving state:", e);
        }
    }

    // Expose to window
    window.playSong = (song) => {
        console.log("[Player] Received play command from song card");
        loadSong(song);
    };
});

/////////////////////////////////////////////////////////////////////////////////////////////