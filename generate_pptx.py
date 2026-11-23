import os
import sys
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR
from pptx.enum.shapes import MSO_SHAPE

# -----------------------------------------------------------------------------
# SYMMETRICAL, SPACED, MINIMALIST PRESENTATION (16:9 Widescreen: 13.333 x 7.5 in)
# -----------------------------------------------------------------------------
prs = Presentation()
prs.slide_width = Inches(13.333)
prs.slide_height = Inches(7.5)

# Balanced Color Palette
BG_COLOR = RGBColor(248, 249, 251)         # Soft off-white canvas
CARD_BG = RGBColor(255, 255, 255)          # Clean card surface
CARD_BORDER = RGBColor(226, 232, 240)      # 1px border Slate-200
HIGHLIGHT_BG = RGBColor(240, 253, 244)     # Subtle emerald tint
HIGHLIGHT_BORDER = RGBColor(187, 247, 208) # Emerald border

TEXT_TITLE = RGBColor(15, 23, 42)          # Slate 900
TEXT_BODY = RGBColor(51, 65, 85)           # Slate 700
TEXT_MUTED = RGBColor(100, 116, 139)       # Slate 500
BRAND_GREEN = RGBColor(22, 101, 52)        # Forest Emerald #166534
BRAND_ACCENT = RGBColor(194, 65, 12)       # Terracotta/Amber
BRAND_RED = RGBColor(185, 28, 28)          # Crimson Red

# Symmetrical Layout Constants
MARGIN_X = Inches(1.0)
CONTENT_WIDTH = Inches(11.333)
HEADER_TOP = Inches(0.65)
TITLE_TOP = Inches(0.95)
SUBTITLE_TOP = Inches(1.50)
CONTENT_TOP = Inches(2.25)
CONTENT_HEIGHT = Inches(4.30)
FOOTER_TOP = Inches(6.85)

def set_slide_bg(slide):
    bg = slide.background
    fill = bg.fill
    fill.solid()
    fill.fore_color.rgb = BG_COLOR

def add_header(slide, part_label, title_text, subtitle_text=None):
    # Eyebrow / Category
    box = slide.shapes.add_textbox(MARGIN_X, HEADER_TOP, CONTENT_WIDTH, Inches(0.3))
    tf = box.text_frame
    tf.word_wrap = True
    tf.margin_left = tf.margin_top = tf.margin_right = tf.margin_bottom = 0
    p = tf.paragraphs[0]
    p.text = part_label.upper()
    p.font.name = "Arial"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = BRAND_GREEN

    # Title
    t_box = slide.shapes.add_textbox(MARGIN_X, TITLE_TOP, CONTENT_WIDTH, Inches(0.6))
    tf2 = t_box.text_frame
    tf2.word_wrap = True
    tf2.margin_left = tf2.margin_top = tf2.margin_right = tf2.margin_bottom = 0
    p2 = tf2.paragraphs[0]
    p2.text = title_text
    p2.font.name = "Arial"
    p2.font.size = Pt(28)
    p2.font.bold = True
    p2.font.color.rgb = TEXT_TITLE

    # Subtitle
    if subtitle_text:
        s_box = slide.shapes.add_textbox(MARGIN_X, SUBTITLE_TOP, CONTENT_WIDTH, Inches(0.5))
        tf3 = s_box.text_frame
        tf3.word_wrap = True
        tf3.margin_left = tf3.margin_top = tf3.margin_right = tf3.margin_bottom = 0
        p3 = tf3.paragraphs[0]
        p3.text = subtitle_text
        p3.font.name = "Arial"
        p3.font.size = Pt(14)
        p3.font.color.rgb = TEXT_MUTED

def add_card(slide, left, top, width, height, is_highlight=False):
    shape = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, left, top, width, height)
    shape.fill.solid()
    shape.fill.fore_color.rgb = HIGHLIGHT_BG if is_highlight else CARD_BG
    shape.line.color.rgb = HIGHLIGHT_BORDER if is_highlight else CARD_BORDER
    shape.line.width = Pt(1)
    return shape

def add_footer(slide, current_slide, total_slides=12):
    box = slide.shapes.add_textbox(MARGIN_X, FOOTER_TOP, CONTENT_WIDTH, Inches(0.35))
    tf = box.text_frame
    tf.word_wrap = True
    tf.margin_left = tf.margin_top = tf.margin_right = tf.margin_bottom = 0
    p = tf.paragraphs[0]
    p.text = f"Mizizi • Group 1 (Course 3.2C Mobile Development)                                                                       Slide {str(current_slide).zfill(2)} / {str(total_slides).zfill(2)}"
    p.font.name = "Arial"
    p.font.size = Pt(10)
    p.font.color.rgb = TEXT_MUTED

def add_notes(slide, notes):
    pass

# -----------------------------------------------------------------------------
# SLIDE 1: COVER
# -----------------------------------------------------------------------------
s1 = prs.slides.add_slide(prs.slide_layouts[6])
set_slide_bg(s1)

# Centered hero block
hero_box = s1.shapes.add_textbox(MARGIN_X, Inches(1.4), CONTENT_WIDTH, Inches(2.8))
tf = hero_box.text_frame
tf.word_wrap = True

p = tf.paragraphs[0]
p.text = "MOBILE DEVELOPMENT (COURSE 3.2C) • FINAL PRESENTATION & DEFENCE"
p.font.name = "Arial"
p.font.size = Pt(11)
p.font.bold = True
p.font.color.rgb = BRAND_GREEN
p.space_after = Pt(12)

p = tf.add_paragraph()
p.text = "Mizizi: Language Practice App"
p.font.name = "Arial"
p.font.size = Pt(44)
p.font.bold = True
p.font.color.rgb = TEXT_TITLE

p = tf.add_paragraph()
p.text = "Keeping Kenyan languages usable for the next generation through meaning-based active practice."
p.font.name = "Arial"
p.font.size = Pt(18)
p.font.color.rgb = TEXT_MUTED
p.space_before = Pt(8)

# Symmetrical Team Table Card
add_card(s1, MARGIN_X, Inches(4.4), CONTENT_WIDTH, Inches(2.1))
tbox = s1.shapes.add_textbox(MARGIN_X + Inches(0.4), Inches(4.6), CONTENT_WIDTH - Inches(0.8), Inches(1.7))
tf = tbox.text_frame
tf.word_wrap = True

p = tf.paragraphs[0]
p.text = "GROUP 1 TEAM MEMBERS & ENGINEERING MODULES"
p.font.name = "Arial"
p.font.size = Pt(11)
p.font.bold = True
p.font.color.rgb = BRAND_GREEN
p.space_after = Pt(8)

p = tf.add_paragraph()
p.text = "• Allan Ngugi (191250): Architecture, Room Database, Repository, ViewModel\n• Ziki Mtula (192992): Practice Flow, Question Counter, Live Scoring Engine\n• Michael Asingwire (192483): Result Screen, Safe Args Navigation, Keyword Matching\n• Kelly Wambui (190500): Archive Screen, RecyclerView with DiffUtil, Delete Action\n• Daniella Thande (152581): Proverb Editor, Dual-mode Create/Update, Input Validation\n• Evelyn Mwangi (190051): Proverb of the Day, MyMemory API Client, WorkManager Reminder\n• Daniel Karanja (180761): Documentation, Screenshots, Signed Release Verification"
p.font.name = "Arial"
p.font.size = Pt(10.5)
p.font.color.rgb = TEXT_BODY

add_footer(s1, 1)
add_notes(s1, "Welcome examiners and fellow students. We are Group 1 presenting Mizizi. Today we will walk you through the problem, our proposed solution, system architecture, a live demonstration, and our technical defence.")

# -----------------------------------------------------------------------------
# SLIDE 2: PART 1 - THE PROBLEM
# -----------------------------------------------------------------------------
s2 = prs.slides.add_slide(prs.slide_layouts[6])
set_slide_bg(s2)
add_header(s2, "Part 1 — Problem", "The Problem: Passive Fluency & Language Attrition", 
           "Young urban Kenyans can understand their mother tongue when spoken to, but lack confidence to write, translate, or speak it.")

col3_w = Inches(3.511)
gap3 = Inches(0.4)

p_cards = [
    ("1. Receptive Gap", 
     "Passive comprehension vs. active expression:\nMany urban Kenyans understand conversational speech from parents or elders, but freeze when asked to construct sentences or translate proverbs actively. Without active practice, confidence never develops."),
    ("2. Literal Machine Failure", 
     "Generic translation tools fail on proverbs:\nTools like Google Translate translate word-for-word. For African proverbs (methali), literal machine translation strips the philosophical lesson, metaphor, and moral context entirely."),
    ("3. The Feedback Void", 
     "Lack of immediate, reliable validation:\nElders and native speakers are not always available to grade attempts. Static reference books offer zero feedback on a student's own attempts, leaving mistakes uncorrected.")
]

for i, (title, desc) in enumerate(p_cards):
    x = MARGIN_X + i * (col3_w + gap3)
    add_card(s2, x, CONTENT_TOP, col3_w, CONTENT_HEIGHT)
    box = s2.shapes.add_textbox(x + Inches(0.35), CONTENT_TOP + Inches(0.35), col3_w - Inches(0.7), CONTENT_HEIGHT - Inches(0.7))
    tf = box.text_frame
    tf.word_wrap = True
    
    p = tf.paragraphs[0]
    p.text = f"0{i+1}"
    p.font.name = "Arial"
    p.font.size = Pt(24)
    p.font.bold = True
    p.font.color.rgb = BRAND_GREEN
    p.space_after = Pt(12)
    
    p = tf.add_paragraph()
    p.text = title
    p.font.name = "Arial"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = TEXT_TITLE
    p.space_after = Pt(10)
    
    p = tf.add_paragraph()
    p.text = desc
    p.font.name = "Arial"
    p.font.size = Pt(12)
    p.font.color.rgb = TEXT_BODY

add_footer(s2, 2)
add_notes(s2, "This slide breaks down the core problem into three clear points: the gap between hearing and speaking, the failure of generic literal translators, and the lack of accessible feedback loops.")

# -----------------------------------------------------------------------------
# SLIDE 3: PART 1 - TARGET USERS & WHY IT MATTERS
# -----------------------------------------------------------------------------
s3 = prs.slides.add_slide(prs.slide_layouts[6])
set_slide_bg(s3)
add_header(s3, "Part 1 — Audience & Significance", "Target Users & Why the Problem Matters", 
           "Empowering urban youth and young parents while preserving indigenous cultural heritage.")

col2_w = Inches(5.416)
gap2 = Inches(0.5)

# Left Column: Target Users
add_card(s3, MARGIN_X, CONTENT_TOP, col2_w, CONTENT_HEIGHT)
box = s3.shapes.add_textbox(MARGIN_X + Inches(0.4), CONTENT_TOP + Inches(0.4), col2_w - Inches(0.8), CONTENT_HEIGHT - Inches(0.8))
tf = box.text_frame
tf.word_wrap = True

p = tf.paragraphs[0]
p.text = "TARGET USERS"
p.font.name = "Arial"
p.font.size = Pt(13)
p.font.bold = True
p.font.color.rgb = BRAND_GREEN
p.space_after = Pt(14)

p = tf.add_paragraph()
p.text = "• Urban Youth & Professionals (18–35):\nLiving in Nairobi, Kisumu, and Mombasa. Communicate predominantly in English and Sheng in school and work, but want a private, judgment-free space to rebuild mother-tongue fluency."
p.font.name = "Arial"
p.font.size = Pt(13)
p.font.color.rgb = TEXT_BODY
p.space_after = Pt(14)

p = tf.add_paragraph()
p.text = "• Young Urban Parents:\nParents raising children in metropolitan homes who want to teach authentic indigenous idioms and moral sayings, but need an authoritative reference to verify correct meanings themselves."
p.font.name = "Arial"
p.font.size = Pt(13)
p.font.color.rgb = TEXT_BODY

# Right Column: Why it Matters
add_card(s3, MARGIN_X + col2_w + gap2, CONTENT_TOP, col2_w, CONTENT_HEIGHT, is_highlight=True)
box = s3.shapes.add_textbox(MARGIN_X + col2_w + gap2 + Inches(0.4), CONTENT_TOP + Inches(0.4), col2_w - Inches(0.8), CONTENT_HEIGHT - Inches(0.8))
tf = box.text_frame
tf.word_wrap = True

p = tf.paragraphs[0]
p.text = "WHY THE PROBLEM MATTERS"
p.font.name = "Arial"
p.font.size = Pt(13)
p.font.bold = True
p.font.color.rgb = BRAND_GREEN
p.space_after = Pt(14)

p = tf.add_paragraph()
p.text = "• Cultural Heritage Preservation:\nProverbs (methali) carry centuries of legal, philosophical, and ethical wisdom. If young generations lose the ability to understand them, centuries of cultural identity erode."
p.font.name = "Arial"
p.font.size = Pt(13)
p.font.color.rgb = TEXT_BODY
p.space_after = Pt(14)

p = tf.add_paragraph()
p.text = "• Active Recall vs. Passive Reading:\nEducational cognitive research shows that active practice (attempting to translate and getting feedback) creates 50%+ stronger memory retention than simply reading passive word lists."
p.font.name = "Arial"
p.font.size = Pt(13)
p.font.color.rgb = TEXT_BODY

add_footer(s3, 3)
add_notes(s3, "Notice the distinction between our two main user personas: urban youth seeking personal fluency, and young parents teaching the next generation. Active practice solves retention.")

# -----------------------------------------------------------------------------
# SLIDE 4: PART 2 - PROPOSED SOLUTION
# -----------------------------------------------------------------------------
s4 = prs.slides.add_slide(prs.slide_layouts[6])
set_slide_bg(s4)
add_header(s4, "Part 2 — Proposed Solution", "Mizizi: Meaning-Based Active Translation Companion", 
           "Active translation practice, semantic keyword grading, and automated daily habit formation.")

# Left Card: Core Description & How it Addresses the Problem
left_w = Inches(6.0)
right_w = Inches(4.9)
gap_mid = Inches(0.433)

add_card(s4, MARGIN_X, CONTENT_TOP, left_w, CONTENT_HEIGHT)
box = s4.shapes.add_textbox(MARGIN_X + Inches(0.4), CONTENT_TOP + Inches(0.35), left_w - Inches(0.8), CONTENT_HEIGHT - Inches(0.7))
tf = box.text_frame
tf.word_wrap = True

p = tf.paragraphs[0]
p.text = "HOW MIZIZI SOLVES THE PROBLEM"
p.font.name = "Arial"
p.font.size = Pt(12)
p.font.bold = True
p.font.color.rgb = BRAND_GREEN
p.space_after = Pt(10)

features_p2 = [
    ("1. Active Translation Engine", "Users translate proverbs in their own words rather than passively reading answers. Live score tracking measures progress."),
    ("2. Meaning-Based Grading", "Evaluates attempts based on concept keywords rather than exact text matching, providing feedback: Correct, Close, or Incorrect."),
    ("3. Machine Translation Contrast", "Calls the MyMemory API to show the literal machine translation side-by-side, exposing why literal translation fails in African proverbs."),
    ("4. Daily Habit Loop", "Uses WorkManager to schedule daily notifications, turning occasional curiosity into a consistent 2-minute daily learning habit.")
]

for title, desc in features_p2:
    p = tf.add_paragraph()
    p.text = f"• {title}:"
    p.font.name = "Arial"
    p.font.size = Pt(12)
    p.font.bold = True
    p.font.color.rgb = TEXT_TITLE
    
    p = tf.add_paragraph()
    p.text = f"  {desc}"
    p.font.name = "Arial"
    p.font.size = Pt(11)
    p.font.color.rgb = TEXT_BODY
    p.space_after = Pt(4)

# Right: Symmetrical Phone Screenshot Showcase
add_card(s4, MARGIN_X + left_w + gap_mid, CONTENT_TOP, right_w, CONTENT_HEIGHT)
try:
    s4.shapes.add_picture("docs/screenshots/practice.png", MARGIN_X + left_w + gap_mid + Inches(0.4), CONTENT_TOP + Inches(0.25), Inches(1.85), CONTENT_HEIGHT - Inches(0.5))
    s4.shapes.add_picture("docs/screenshots/result.png", MARGIN_X + left_w + gap_mid + Inches(2.65), CONTENT_TOP + Inches(0.25), Inches(1.85), CONTENT_HEIGHT - Inches(0.5))
except Exception as e:
    print(f"Error loading picture: {e}")

add_footer(s4, 4)
add_notes(s4, "Here you see how Mizizi addresses the problem directly: users are actively challenged to translate proverbs, receive instant semantic feedback, and see the contrast with literal machine translation.")

# -----------------------------------------------------------------------------
# SLIDE 5: PART 2 - MAIN FEATURES
# -----------------------------------------------------------------------------
s5 = prs.slides.add_slide(prs.slide_layouts[6])
set_slide_bg(s5)
add_header(s5, "Part 2 — Core Features", "The Five Functional Pillars of Mizizi", 
           "Complete user journeys designed for daily practice, review, authoring, and reminders.")

col4_w = Inches(2.608)
gap4 = Inches(0.3)

pillars = [
    ("Practice Engine", "Interactive Sessions", 
     "• 5-question random sessions\n• Live question counter\n• Live score tracking\n• Optional hint request\n• Instant grade badge"),
    ("Semantic Result", "Keyword Analysis", 
     "• Tiered evaluation\n• Displays user answer\n• Shows true meaning\n• Highlights matched keywords\n• MyMemory translation card"),
    ("Personal Archive", "Persistent History", 
     "• Newest attempts first\n• RecyclerView with DiffUtil\n• Shows proverb & grade\n• Item-level deletion\n• Clear-all with dialog"),
    ("Proverb Editor", "Full CRUD Studio", 
     "• Add custom proverbs\n• Edit existing proverbs\n• Custom meaning keywords\n• Inline input validation\n• Delete proverb row")
]

for i, (title, sub, body) in enumerate(pillars):
    x = MARGIN_X + i * (col4_w + gap4)
    add_card(s5, x, CONTENT_TOP, col4_w, CONTENT_HEIGHT)
    box = s5.shapes.add_textbox(x + Inches(0.25), CONTENT_TOP + Inches(0.3), col4_w - Inches(0.5), CONTENT_HEIGHT - Inches(0.6))
    tf = box.text_frame
    tf.word_wrap = True
    
    p = tf.paragraphs[0]
    p.text = f"PILLAR 0{i+1}"
    p.font.name = "Arial"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = BRAND_GREEN
    p.space_after = Pt(8)
    
    p = tf.add_paragraph()
    p.text = title
    p.font.name = "Arial"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = TEXT_TITLE
    
    p = tf.add_paragraph()
    p.text = sub
    p.font.name = "Arial"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = TEXT_MUTED
    p.space_after = Pt(12)
    
    p = tf.add_paragraph()
    p.text = body
    p.font.name = "Arial"
    p.font.size = Pt(11.5)
    p.font.color.rgb = TEXT_BODY

add_footer(s5, 5)
add_notes(s5, "Mizizi provides a complete learning cycle: testing yourself in Practice, analyzing results, reviewing past attempts in Archive, and authoring new community proverbs in Editor.")

# -----------------------------------------------------------------------------
# SLIDE 6: PART 3 - SYSTEM ARCHITECTURE (DIAGRAM SLIDE)
# -----------------------------------------------------------------------------
s6 = prs.slides.add_slide(prs.slide_layouts[6])
set_slide_bg(s6)
add_header(s6, "Part 3 — System Architecture", "System Architecture & Layering Diagram", 
           "Clean separation of concerns: UI Layer, ViewModel Layer, Repository, and Data Layer.")

# Embed the uploaded Architecture Diagram prominently!
diag_card_w = Inches(11.333)
diag_card_h = CONTENT_HEIGHT

add_card(s6, MARGIN_X, CONTENT_TOP, diag_card_w, diag_card_h)

diag_path = "docs/screenshots/architecture_diagram.png"
if os.path.exists(diag_path):
    # Center the diagram neatly inside the card
    s6.shapes.add_picture(diag_path, MARGIN_X + Inches(0.2), CONTENT_TOP + Inches(0.15), diag_card_w - Inches(0.4), diag_card_h - Inches(0.3))
else:
    print(f"Diagram not found at {diag_path}")

add_footer(s6, 6)
add_notes(s6, "Here is our complete system architecture diagram as requested. Notice the strict unidirectional flow: user actions flow from UI to ViewModel, business logic is resolved in SessionEngine and Grader, and the Repository delegates to Room SQLite and the MyMemory API.")

# -----------------------------------------------------------------------------
# SLIDE 7: PART 3 - ARCHITECTURAL COMPONENTS EXPLAINED
# -----------------------------------------------------------------------------
s7 = prs.slides.add_slide(prs.slide_layouts[6])
set_slide_bg(s7)
add_header(s7, "Part 3 — Architectural Responsibilities", "Layer Responsibilities: UI, ViewModel, Repository & Data", 
           "Each component has exactly one well-defined responsibility under modern Android guidelines.")

layers_explained = [
    ("1. UI Layer", "Fragments + ViewBinding", 
     "• Fragments inflate XML via View Binding\n• Observes LiveData from ViewModel\n• Collects user inputs & touch events\n• Strictly passive: zero SQL or network calls\n• Single Activity with NavHostFragment"),
    ("2. ViewModel Layer", "MiziziViewModel", 
     "• Scoped to Activity lifecycle (survives rotation)\n• Owns SessionEngine & live score\n• Validates proverb forms before save\n• Domain logic: pure Kotlin Grader.kt\n• Exposes immutable LiveData to UI"),
    ("3. Repository Layer", "MiziziRepository", 
     "• The only door into the data layer\n• Single Source of Truth for the app\n• Hides database & API details from UI\n• Provides clean Coroutine suspend functions\n• Decouples storage from presentation"),
    ("4. Data Layer", "Room SQLite & APIs", 
     "• Room Database: mizizi.db\n• ProverbDao & AttemptDao interfaces\n• Pre-seeded via onCreate() callback\n• MyMemory API over HTTPS (Dispatchers.IO)\n• DailyProverbWorker in WorkManager")
]

for i, (title, sub, body) in enumerate(layers_explained):
    x = MARGIN_X + i * (col4_w + gap4)
    add_card(s7, x, CONTENT_TOP, col4_w, CONTENT_HEIGHT, is_highlight=(i==3))
    box = s7.shapes.add_textbox(x + Inches(0.25), CONTENT_TOP + Inches(0.3), col4_w - Inches(0.5), CONTENT_HEIGHT - Inches(0.6))
    tf = box.text_frame
    tf.word_wrap = True
    
    p = tf.paragraphs[0]
    p.text = title
    p.font.name = "Arial"
    p.font.size = Pt(15)
    p.font.bold = True
    p.font.color.rgb = BRAND_GREEN if i==3 else TEXT_TITLE
    
    p = tf.add_paragraph()
    p.text = sub
    p.font.name = "Arial"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = BRAND_ACCENT
    p.space_after = Pt(10)
    
    p = tf.add_paragraph()
    p.text = body
    p.font.name = "Arial"
    p.font.size = Pt(11)
    p.font.color.rgb = TEXT_BODY

add_footer(s7, 7)
add_notes(s7, "No layer violates its boundaries. The Fragment only knows the ViewModel; the ViewModel only knows the Repository; and only the Repository interacts with Room and the network.")

# -----------------------------------------------------------------------------
# SLIDE 8: PART 3 - UNIDIRECTIONAL DATA FLOW
# -----------------------------------------------------------------------------
s8 = prs.slides.add_slide(prs.slide_layouts[6])
set_slide_bg(s8)
add_header(s8, "Part 3 — Unidirectional Data Flow", "Data Flow: Tracing an Answer from UI to SQLite", 
           "Tracing an attempt: from touch event, through pure domain logic, into SQLite persistence.")

flow_steps_p3 = [
    ("Step 01: UI Event", "User Submits Answer", 
     "• User types translation in PracticeFragment\n• Taps Submit button\n• Fragment calls:\n  viewModel.submitAnswer(answer)\n• Zero database code in Fragment"),
    ("Step 02: Business Logic", "Pure Kotlin Grader", 
     "• ViewModel passes text to Grader.kt\n• Grader normalizes casing & punctuation\n• Matches answer against concept keywords\n• Returns Grade: CORRECT, CLOSE, INCORRECT\n• Updates _outcome & _session LiveData"),
    ("Step 03: Repository", "Async Room Write", 
     "• ViewModel launches on viewModelScope\n• Calls repository.recordAttempt()\n• AttemptDao executes SQL on background thread:\n  INSERT INTO attempts (proverbId, userAnswer, grade, createdAt)"),
    ("Step 04: Reactive UI", "LiveData Auto-Update", 
     "• Room detects attempts table change\n• AttemptDao.observeAll() automatically re-emits updated list\n• Home and Archive screens refresh reactively\n• No manual polling required")
]

for i, (step, title, body) in enumerate(flow_steps_p3):
    x = MARGIN_X + i * (col4_w + gap4)
    add_card(s8, x, CONTENT_TOP, col4_w, CONTENT_HEIGHT, is_highlight=(i==3))
    box = s8.shapes.add_textbox(x + Inches(0.25), CONTENT_TOP + Inches(0.3), col4_w - Inches(0.5), CONTENT_HEIGHT - Inches(0.6))
    tf = box.text_frame
    tf.word_wrap = True
    
    p = tf.paragraphs[0]
    p.text = step
    p.font.name = "Arial"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = BRAND_GREEN
    p.space_after = Pt(4)
    
    p = tf.add_paragraph()
    p.text = title
    p.font.name = "Arial"
    p.font.size = Pt(15)
    p.font.bold = True
    p.font.color.rgb = TEXT_TITLE
    p.space_after = Pt(10)
    
    p = tf.add_paragraph()
    p.text = body
    p.font.name = "Arial"
    p.font.size = Pt(11)
    p.font.color.rgb = TEXT_BODY

add_footer(s8, 8)
add_notes(s8, "This slide explains how data moves in one direction. Room's observable LiveData means the UI never needs to poll or manually refresh after a database insert.")

# -----------------------------------------------------------------------------
# SLIDE 9: PART 4 - APPLICATION DEMONSTRATION RUN-SHEET
# -----------------------------------------------------------------------------
s9 = prs.slides.add_slide(prs.slide_layouts[6])
set_slide_bg(s9)
add_header(s9, "Part 4 — Application Demonstration", "Live Working Functionality Demonstration Guide", 
           "Run live on physical device / emulator to demonstrate working functionality rather than static screenshots.")

# Left 6-step checklist card
add_card(s9, MARGIN_X, CONTENT_TOP, left_w, CONTENT_HEIGHT)
box = s9.shapes.add_textbox(MARGIN_X + Inches(0.4), CONTENT_TOP + Inches(0.35), left_w - Inches(0.8), CONTENT_HEIGHT - Inches(0.7))
tf = box.text_frame
tf.word_wrap = True

p = tf.paragraphs[0]
p.text = "DEMONSTRATION STEP-BY-STEP CHECKLIST"
p.font.name = "Arial"
p.font.size = Pt(12)
p.font.bold = True
p.font.color.rgb = BRAND_GREEN
p.space_after = Pt(8)

demo_script = [
    ("1. Seed Data & Counters", "Launch app fresh. Point out 8 pre-seeded proverbs from SQLite and 0 completed attempts on Home."),
    ("2. Proverb Practice Flow", "Start practice. Read 'Haraka haraka haina baraka'. Type answer. Submit and show live score update."),
    ("3. Results & Machine Contrast", "Open Result screen. Point out matched keywords badge and live MyMemory machine translation card."),
    ("4. Archive Verification", "Open Archive. Show history logged newest first with timestamps. Delete one attempt to verify deletion."),
    ("5. Proverb Editor CRUD", "Open Editor. Submit empty fields (show inline validation). Enter valid custom proverb. Verify count is 9."),
    ("6. WorkManager Reminder", "Open Daily Proverb screen. Toggle daily reminder switch. Verify background worker scheduling.")
]

for title, desc in demo_script:
    p = tf.add_paragraph()
    p.text = f"• {title}:"
    p.font.name = "Arial"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = TEXT_TITLE
    
    p = tf.add_paragraph()
    p.text = f"  {desc}"
    p.font.name = "Arial"
    p.font.size = Pt(10.5)
    p.font.color.rgb = TEXT_BODY
    p.space_after = Pt(3)

# Right: Screenshot preview of Editor and Notification
add_card(s9, MARGIN_X + left_w + gap_mid, CONTENT_TOP, right_w, CONTENT_HEIGHT)
try:
    s9.shapes.add_picture("docs/screenshots/editor.png", MARGIN_X + left_w + gap_mid + Inches(0.4), CONTENT_TOP + Inches(0.25), Inches(1.85), CONTENT_HEIGHT - Inches(0.5))
    s9.shapes.add_picture("docs/screenshots/notification.png", MARGIN_X + left_w + gap_mid + Inches(2.65), CONTENT_TOP + Inches(0.25), Inches(1.85), CONTENT_HEIGHT - Inches(0.5))
except Exception as e:
    print(f"Error loading picture: {e}")

add_footer(s9, 9)
add_notes(s9, "Now we switch to our live device demonstration. We follow these 6 exact operational steps to prove working functionality to the examiners.")

# -----------------------------------------------------------------------------
# SLIDE 10: PART 5 - TECHNICAL DEFENCE (QUESTIONS 1 - 4)
# -----------------------------------------------------------------------------
s10 = prs.slides.add_slide(prs.slide_layouts[6])
set_slide_bg(s10)
add_header(s10, "Part 5 — Technical Defence (Q1 – Q4)", "Technical Defence: Architecture, Database, CRUD & Validation", 
           "Direct, simple technical answers backed by actual code implementation.")

card_w_q = Inches(5.416)
card_h_q = Inches(1.95)
gap_v_q = Inches(0.3)

q_1_4 = [
    ("Why did you choose this architecture?", 
     "• MVVM + Repository Pattern ensures strict separation of concerns.\n• Lifecycle Awareness: MiziziViewModel is scoped to activityViewModels(), preserving practice sessions and scores across screen rotations.\n• Fast Unit Testing: Grader.kt is pure Kotlin, allowing 9 tests to run in milliseconds without mocking Android dependencies."),
    ("Why did you choose this database?", 
     "• Room Persistence Library provides compile-time verification of raw SQL queries, eliminating runtime syntax crashes.\n• Native LiveData Integration: DAOs return LiveData streams that automatically update the UI when tables change.\n• Safe First-Run Seeding: RoomDatabase.Callback.onCreate() populates 8 seed proverbs only once when the SQLite file is created."),
    ("How does your CRUD functionality work?", 
     "• Create: ProverbDao.insert() with auto-generated ID (Long = 0) and AttemptDao.insert() for logs.\n• Read: ProverbDao.observeAll() and AttemptDao.observeAll() returning observable LiveData.\n• Update: ProverbEditorFragment pre-fills entity fields and calls ProverbDao.update().\n• Delete: ProverbDao.delete() in editor; AttemptDao.deleteById() and clear() in Archive."),
    ("What happens when the user enters invalid information?", 
     "• Centralized Validation: validateProverb() in ViewModel enforces non-blank fields and a minimum length of 6 characters.\n• UI Feedback: Emits FormResult.Invalid(message), displaying an inline red error banner without losing user input.\n• Attempt Grading: Grader.normalize() strips punctuation; empty answers safely receive INCORRECT without crashing.")
]

coords_q = [
    (MARGIN_X, CONTENT_TOP),
    (MARGIN_X + card_w_q + gap2, CONTENT_TOP),
    (MARGIN_X, CONTENT_TOP + card_h_q + gap_v_q),
    (MARGIN_X + card_w_q + gap2, CONTENT_TOP + card_h_q + gap_v_q)
]

for i, (title, desc) in enumerate(q_1_4):
    cx, cy = coords_q[i]
    add_card(s10, cx, cy, card_w_q, card_h_q)
    box = s10.shapes.add_textbox(cx + Inches(0.3), cy + Inches(0.15), card_w_q - Inches(0.6), card_h_q - Inches(0.3))
    tf = box.text_frame
    tf.word_wrap = True
    
    p = tf.paragraphs[0]
    p.text = f"Q{i+1}: {title}"
    p.font.name = "Arial"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = BRAND_GREEN
    p.space_after = Pt(4)
    
    p = tf.add_paragraph()
    p.text = desc
    p.font.name = "Arial"
    p.font.size = Pt(9.5)
    p.font.color.rgb = TEXT_BODY

add_footer(s10, 10)
add_notes(s10, "Questions 1 through 4 explain our MVVM lifecycle stability, Room compile-time query verification, clean DAO CRUD methods, and ViewModel input validation.")

# -----------------------------------------------------------------------------
# SLIDE 11: PART 5 - TECHNICAL DEFENCE (QUESTIONS 5 - 8)
# -----------------------------------------------------------------------------
s11 = prs.slides.add_slide(prs.slide_layouts[6])
set_slide_bg(s11)
add_header(s11, "Part 5 — Technical Defence (Q5 – Q8)", "Technical Defence: Data Movement, API, WorkManager & Next Steps", 
           "Resilience patterns, background scheduling, and future engineering roadmap.")

q_5_8 = [
    ("How does data move from the UI to the database?", 
     "1. UI Event: Fragment collects user input and calls ViewModel.\n2. Coroutine Dispatch: ViewModel launches on viewModelScope (Main).\n3. Repository Delegation: Repository maps parameters to ProverbEntity.\n4. Room Background Thread: DAO writes to SQLite asynchronously.\n5. Invalidation Tracker: Room triggers automatic LiveData update to UI."),
    ("What happens when the API is unavailable?", 
     "• Sealed ApiResult Pattern: Wraps network calls in ApiResult.Success or ApiResult.Error.\n• Strict Timeouts: HttpURLConnection enforces 8-second connect and read timeouts.\n• Offline-First Design: Network failure displays a graceful error message card; practice, grading, and archive remain 100% operational offline."),
    ("Why did you use WorkManager?", 
     "• Guaranteed Persistence: Survives app process death and device reboots, unlike standard Handlers or Timers.\n• Battery / Doze Optimization: Daily cultural reminders are deferrable; the OS batches execution to save battery.\n• Idempotent Policy: ExistingPeriodicWorkPolicy.KEEP (24h period) prevents duplicate registered workers."),
    ("What would you improve if you had more development time?", 
     "1. Multi-Vernacular Support: Expand beyond Swahili to Kikuyu, Dholuo, Luhya, Kalenjin, Kamba with native audio pronunciation.\n2. On-Device NLP: Replace keyword matching with local TensorFlow Lite embeddings for deeper semantic similarity.\n3. Cloud Sync: Firebase Auth & Firestore for community-contributed proverbs.")
]

for i, (title, desc) in enumerate(q_5_8):
    cx, cy = coords_q[i]
    add_card(s11, cx, cy, card_w_q, card_h_q, is_highlight=(i==3))
    box = s11.shapes.add_textbox(cx + Inches(0.3), cy + Inches(0.15), card_w_q - Inches(0.6), card_h_q - Inches(0.3))
    tf = box.text_frame
    tf.word_wrap = True
    
    p = tf.paragraphs[0]
    p.text = f"Q{i+5}: {title}"
    p.font.name = "Arial"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = BRAND_GREEN if i==3 else BRAND_ACCENT
    p.space_after = Pt(4)
    
    p = tf.add_paragraph()
    p.text = desc
    p.font.name = "Arial"
    p.font.size = Pt(9.5)
    p.font.color.rgb = TEXT_BODY

add_footer(s11, 11)
add_notes(s11, "Questions 5 through 8 demonstrate our unidirectional coroutine flow, sealed ApiResult network resilience, WorkManager reboot survivability, and clear future vision.")

# -----------------------------------------------------------------------------
# SLIDE 12: CONCLUSION & VERIFICATION
# -----------------------------------------------------------------------------
s12 = prs.slides.add_slide(prs.slide_layouts[6])
set_slide_bg(s12)
add_header(s12, "Conclusion & Verification", "Engineering Integrity & Release Verification", 
           "Every member designed, implemented, and committed a distinct architectural slice.")

# Left Card: Contributions
add_card(s12, MARGIN_X, CONTENT_TOP, left_w, CONTENT_HEIGHT)
box = s12.shapes.add_textbox(MARGIN_X + Inches(0.4), CONTENT_TOP + Inches(0.35), left_w - Inches(0.8), CONTENT_HEIGHT - Inches(0.7))
tf = box.text_frame
tf.word_wrap = True

p = tf.paragraphs[0]
p.text = "VERIFIED INDIVIDUAL COMMITS (FEATURE BRANCH MERGES)"
p.font.name = "Arial"
p.font.size = Pt(11)
p.font.bold = True
p.font.color.rgb = BRAND_GREEN
p.space_after = Pt(10)

commits_s12 = [
    ("Allan Ngugi (191250)", "423bece", "Setup, Room DB, Repository, ViewModel, Navigation shell"),
    ("Ziki Mtula (192992)", "2bed260", "Practice screen question flow, answer input, inline scoring"),
    ("Michael Asingwire (192483)", "e6c095a", "Result screen, Safe Args, keyword matching breakdown"),
    ("Kelly Wambui (190500)", "80c773c", "Archive RecyclerView, DiffUtil adapter, item deletion"),
    ("Daniella Thande (152581)", "b6ccdc3", "Proverb editor dual-mode form, validation logic"),
    ("Evelyn Mwangi (190051)", "c549827", "Daily proverb, MyMemory HTTP integration, WorkManager"),
    ("Daniel Karanja (180761)", "36a31b2", "README, release checklist, signed APK verification")
]

for name, sha, desc in commits_s12:
    p = tf.add_paragraph()
    p.text = f"• {name} [{sha}]:"
    p.font.name = "Arial"
    p.font.size = Pt(10.5)
    p.font.bold = True
    p.font.color.rgb = TEXT_TITLE
    
    p = tf.add_paragraph()
    p.text = f"  {desc}"
    p.font.name = "Arial"
    p.font.size = Pt(10)
    p.font.color.rgb = TEXT_BODY
    p.space_after = Pt(2)

# Right: Release Status
add_card(s12, MARGIN_X + left_w + gap_mid, CONTENT_TOP, right_w, CONTENT_HEIGHT, is_highlight=True)
box = s12.shapes.add_textbox(MARGIN_X + left_w + gap_mid + Inches(0.4), CONTENT_TOP + Inches(0.6), right_w - Inches(0.8), CONTENT_HEIGHT - Inches(1.2))
tf = box.text_frame
tf.word_wrap = True

p = tf.paragraphs[0]
p.text = "RELEASE v1.0.0 VERIFIED"
p.font.name = "Arial"
p.font.size = Pt(11)
p.font.bold = True
p.font.color.rgb = BRAND_GREEN
p.space_after = Pt(12)

p = tf.add_paragraph()
p.text = "Production Ready."
p.font.name = "Arial"
p.font.size = Pt(26)
p.font.bold = True
p.font.color.rgb = TEXT_TITLE
p.space_after = Pt(12)

p = tf.add_paragraph()
p.text = "• Signed APK verified on Android API 24+\n• 9/9 Gradle Unit Tests Passing\n• Complete MVVM Layering with Room & API\n\nThank you for your time.\nWe welcome questions & technical discussion."
p.font.name = "Arial"
p.font.size = Pt(12)
p.font.color.rgb = TEXT_BODY

add_footer(s12, 12)
add_notes(s12, "In conclusion, our codebase demonstrates clear individual contributions, unit tests passing, and a signed production APK. Thank you for your time. We are now open for technical discussion.")

# -----------------------------------------------------------------------------
# SAVE
# -----------------------------------------------------------------------------
out_file = "Mizizi_Presentation_Group_1.pptx"
prs.save(out_file)
os.system(f"cp {out_file} docs/{out_file}")
print(f"Generated clean symmetrical PPTX at: {out_file}")
