import { useMemo, useState } from 'react'
import SelfCare from '../components/SelfCare.jsx'
import FocusTimer from '../components/FocusTimer.jsx'
import Grounding from '../components/Grounding.jsx'
import Meditation from '../components/Meditation.jsx'
import Breathing from '../components/Breathing.jsx'

/**
 * Slides in exact required order.
 *
 * Navigation requirements:
 * - useState controls which section + slide we're on
 * - Next/Previous work correctly
 * - Previous disabled on first slide of each section
 * - No layout shift: fixed outer container and slide-content min-height
 */
export default function ToolsPresentation() {
  const sections = useMemo(
    () => [
      { key: 'selfCare', title: 'SELF CARE', slideCount: 2, Component: SelfCare },
      { key: 'focusTimer', title: 'FOCUS TIMER', slideCount: 2, Component: FocusTimer },
      { key: 'grounding', title: '5-4-3-2-1 GROUNDING', slideCount: 6, Component: Grounding }, // 5 steps + well done
      { key: 'meditation', title: 'MEDITATION', slideCount: 2, Component: Meditation },
      { key: 'breathing', title: '4-7-8 BREATHING', slideCount: 2, Component: Breathing },
    ],
    [],
  )

  const [sectionIndex, setSectionIndex] = useState(0)
  const [slideIndex, setSlideIndex] = useState(0)

  const section = sections[sectionIndex]
  const SectionComponent = section.Component

  const goNext = () => {
    // If we're at last slide of section, move to next section start.
    if (slideIndex >= section.slideCount - 1) {
      if (sectionIndex < sections.length - 1) {
        setSectionIndex((i) => i + 1)
        setSlideIndex(0)
      }
      return
    }
    setSlideIndex((s) => s + 1)
  }

  const goPrevious = () => {
    if (slideIndex === 0) return // disabled per requirement
    setSlideIndex((s) => Math.max(0, s - 1))
  }

  const goToSlide = (idx) => {
    const clamped = Math.max(0, Math.min(section.slideCount - 1, idx))
    setSlideIndex(clamped)
  }

  return (
    <div className="min-h-screen bg-bg-splash flex justify-center">
      {/* fixed-width mobile-first canvas to prevent shifting */}
      <div className="w-full max-w-[420px] min-h-screen">
        <SectionComponent
          currentSlide={slideIndex}
          onNext={goNext}
          onPrevious={goPrevious}
          onGoToSlide={goToSlide}
        />
      </div>
    </div>
  )
}

