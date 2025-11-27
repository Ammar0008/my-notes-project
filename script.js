// ===== Notes Data Handling =====
let notes = [];
let activeNoteId = null;

// Load notes from backend API
async function loadNotes() {
    try {
        const response = await fetch("http://localhost:8081/notes");
        notes = await response.json();
    } catch (error) {
        console.error("Failed to load notes:", error);
        notes = [];
    }
    renderNotesList();
}

// Save all notes to backend API
async function saveNotes() {
    try {
        await fetch("http://localhost:8081/notes", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(notes)
        });
    } catch (error) {
        console.error("Failed to save notes:", error);
    }
}

// Render notes in sidebar
function renderNotesList() {
    const list = document.getElementById("notes-list");
    list.innerHTML = "";

    notes.forEach(note => {
        const li = document.createElement("li");
        li.textContent = note.text || "Untitled Note";
        li.dataset.id = note.id;
        if (note.id === activeNoteId) li.classList.add("active");

        li.onclick = () => openNote(note.id);
        list.appendChild(li);
    });
}

// Open a note
function openNote(id) {
    const note = notes.find(n => n.id === id);
    if (!note) return;

    activeNoteId = id;
    const editor = document.getElementById("note-editor");
    editor.value = note.text;
    renderNotesList();
}

// Auto-save when editing
document.getElementById("note-editor").oninput = async function () {
    if (!activeNoteId) return;

    const note = notes.find(n => n.id === activeNoteId);
    note.text = this.value;

    await saveNotes();
    renderNotesList();
};

// Create a new note
document.getElementById("add-note-btn").onclick = async function () {
    const newNote = {
        id: Date.now(),
        text: ""
    };
    notes.push(newNote);
    activeNoteId = newNote.id;

    await saveNotes();
    renderNotesList();
    openNote(newNote.id);
};

// Delete note
document.getElementById("delete-note-btn").onclick = async function () {
    if (!activeNoteId) return;

    notes = notes.filter(n => n.id !== activeNoteId);
    activeNoteId = null;

    document.getElementById("note-editor").value = "";
    await saveNotes();
    renderNotesList();
};

// ===== Theme Toggle =====
document.getElementById("theme-toggle").onclick = function () {
    document.body.classList.toggle("light");
    document.body.classList.toggle("dark");
};

// ===== Initialize App =====
loadNotes();
